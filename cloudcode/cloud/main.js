//Parse Subclass Attendance
var Attendance  = Parse.Object.extend("Attendance",{
  getUser: function () {
    return this.get(Attendance.USER);
  },
  getEvent: function () {
    return this.get(Attendance.EVENT);
  },
  getPingCounts: function () {
    return this.get(Attendance.PING_COUNTS);
  },
  setPingCounts: function(pingCounts) {
    this.set(Attendance.PING_COUNTS,pingCounts);
  },
  recordPing: function (user) {
    var pingCounts = this.getPingCounts();
    if (pingCounts === undefined){
      pingCounts = {};
    }
    var old = pingCounts[user.id];
    if (old === undefined){
      pingCounts[user.id] = 1;
    } else {
      pingCounts[user.id] = old + 1;
    }
    this.setPingCounts(pingCounts);
  },
  setFinished : function () {
    this.set(Attendance.IS_FINISHED,true)
  }
},
{
  MAC : "MAC",
  EVENT : "EVENT",
  USER : "USER",
  PING_COUNTS : "PING_COUNTS",
  HAS_LEFT : "HAS_LEFT",
  IS_FINISHED : "IS_FINISHED"
});

//Parse Subclass Swipe
var Swipe  = Parse.Object.extend("Swipe",{
  isLeftSwipe: function () {
    return this.get(Swipe.IS_LEFT_SWIPE);
  },
  getSwiper: function () {
    return this.get(Swipe.SWIPER);
  },
  getSwipee: function () {
    return this.get(Swipe.SWIPEE);
  },
  getEvent: function() {
    return this.get(Swipe.EVENT);
  }
},
{
  EVENT : "EVENT",
  SWIPER : "SWIPER",
  SWIPEE : "SWIPEE",
  IS_LEFT_SWIPE : "IS_LEFT_SWIPE"
});

//Fields on the _User class
var User = {
  FIRST_NAME : "pr_first_name",
  LAST_NAME : "pr_last_name",
  REG_NAME : "name",
  PROFILE_PICTURE :"pr_profile_picture"
};

//Fields on the _Installation class
var Installation = {
  USER : "user"
};

//Fields on the  JSON passed to the client
var PublicProfile = {
  USER_ID : "user_id",
  FIRST_NAME : User.FIRST_NAME,
  LAST_NAME : User.LAST_NAME,
  PICTURE : "picture_url"
};

//Cloud code function to record the pings from a set of MAC addresses
//args: String attendanceID, String[] MACs
Parse.Cloud.define("recordPings", function(request, response) {
  if (request.params.MACs.length === 0){
      response.success("No pings to save");
  } else {
    var localAttendanceID = request.params.attendanceID;
    var q = new Parse.Query(Attendance);
    var localAttendance;
    var hitCount;
    q.get(localAttendanceID).then(function(result) {
      localAttendance = result;
      var query = new Parse.Query(Attendance);
      query.containedIn(Attendance.MAC, request.params.MACs);
      query.equalTo(Attendance.EVENT,localAttendance.getEvent());
      query.equalTo(Attendance.HAS_LEFT, false);
      query.include(Attendance.USER);
      return query.find();
    }).then(function(results) {
      hitCount = results.length;
      for (var i = 0; i < results.length; i++) {
        var foreignAttendance = results[i];
        foreignAttendance.recordPing(localAttendance.getUser());
        localAttendance.recordPing(foreignAttendance.getUser());
      }
      return Attendance.saveAll(results);
    }).then( function(result){
      return localAttendance.save();
    }).then( function(result){
      response.success("Saved " + hitCount + " pings");
    }, function(error){
      response.error(error.message);
    });
  }
});

//Cloud code function to get a set of public profiles sorted by pings
//args: String attendanceID
Parse.Cloud.define("getSortedProfiles", function(request, response){
  var q = new Parse.Query(Attendance);
  q.include(Attendance.EVENT);
  var attendance;
  q.get(request.params.attendanceID).then(function(result) {
    attendance = result;
    var e = result.getEvent();

    var innerQueryA = new Parse.Query(Swipe);
    innerQueryA.equalTo(Swipe.SWIPER, request.user);
    innerQueryA.equalTo(Swipe.IS_LEFT_SWIPE, false);

    var innerQueryB = new Parse.Query(Swipe);
    innerQueryB.equalTo(Swipe.SWIPER, request.user);
    innerQueryB.equalTo(Swipe.EVENT, e);

    var innerQuery = Parse.Query.or(innerQueryA, innerQueryB);

    var query = new Parse.Query(Attendance);
    query.equalTo(Attendance.EVENT, e);
    query.notEqualTo(Attendance.USER, request.user);
    query.doesNotMatchKeyInQuery(Attendance.USER, Swipe.SWIPEE, innerQuery);
    query.include(Attendance.USER);

    return query.find();
  }).then(function(results) {
    var array = [];
    if (results.length===0){
      var currentTime = new Date();
      var endTime = attendance.getEvent().get("END_DATE");
      if (endTime < currentTime){
        attendance.setFinished();
        attendance.save().then(function(result){
          response.success(array);
        },function(error){
          response.error(error.message);
        });
      } else {
        response.success(array);
      }
    } else {
      var pingCounts = attendance.getPingCounts();
      if (pingCounts !== undefined){
        results.sort(function(a,b){
          var aPings = pingCounts[a.getUser().id];
          var bPings = pingCounts[b.getUser().id];
          if (aPings === undefined){
            aPings = 0;
          }
          if (bPings === undefined){
            bPings = 0;
          }
          return bPings- aPings;
        });
      }
      results.forEach(function(entry){
        var obj = {};
        var user = entry.getUser();
        obj[PublicProfile.USER_ID] = user.id;
        var first_name = user.get(User.FIRST_NAME);
        if (first_name === undefined){
          obj[PublicProfile.FIRST_NAME] = user.get(User.REG_NAME);
          obj[PublicProfile.LAST_NAME] = "";
        } else {
          obj[PublicProfile.FIRST_NAME] = first_name;
          obj[PublicProfile.LAST_NAME] = user.get(User.LAST_NAME);
        }
        var file = user.get(User.PROFILE_PICTURE);
        if (file !== undefined){
          obj[PublicProfile.PICTURE] = file.url();
        }
        array.push(obj);
      });
      response.success(array);
    }
  }, function(error){
    response.error(error.message);
  });
});

//BeforeSave hook on Swipes to mirror the swipee pointer with a string ID
Parse.Cloud.beforeSave(Swipe, function(request,response){
  var swipe = request.object;
  if (swipe.getSwipee().id === swipe.get("swipeeID")){
    response.success();
  } else {
    swipe.set("swipeeID",swipe.getSwipee().id);
    response.success();
  }
});

//Convenience function to get a filled push request from one user to another
function getPush(toUser, fromUser){
  var fullName;
  var firstName = fromUser.get(User.FIRST_NAME);
  if (firstName === undefined) {
    fullName = fromUser.get(User.REG_NAME);
  } else {
    fullName = firstName + " " + fromUser.get(User.LAST_NAME);
  }

  var pushQuery = new Parse.Query(Parse.Installation);
  pushQuery.equalTo(Installation.USER, toUser);
  return Parse.Push.send({
      where : pushQuery,
      data : {
        alert : "You connected with " + fullName ,
        userID : fromUser.id
      }
  });
}

//AfterSave hook on Swipes that finds pairs and sends push notifications
Parse.Cloud.afterSave(Swipe, function(request) {
  var swipe = request.object;
  if (!swipe.isLeftSwipe()){
    var query = new Parse.Query(Swipe);
    query.equalTo(Swipe.SWIPEE, swipe.getSwiper());
    query.equalTo(Swipe.SWIPER, swipe.getSwipee());
    query.equalTo(Swipe.IS_LEFT_SWIPE, false);
    query.include(Swipe.SWIPER);

    query.find().then( function (results) {
      if (results.length ===0){
        console.log("No matches");
      } else if (results.length === 1){
        var connector = swipe.getSwiper();
        var connectee = results[0].getSwiper();

        connector.fetch().then(function(result){
            return getPush(connectee,connector);
        }).then (function (results){
            return getPush(connector,connectee);
        }).then (
          function (results){
            console.log("Pushed to two people");
          }, function(error) {
            console.error(error.message);
          }
        );
      } else {
        console.error("INCONSISTENT STATE");
      }
    });
  }
});
