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
  }
},
{
  MAC : "MAC",
  EVENT : "EVENT",
  USER : "USER",
  PING_COUNTS : "PING_COUNTS",
  HAS_LEFT : "HAS_LEFT"
});
Parse.Cloud.define("recordPings", function(request, response) {
  if (request.params.MACs.length === 0) response.success("No pings to save");
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
});
var Swipe  = Parse.Object.extend("Swipe",{
  //Class properties/methods
},
{
  EVENT : "EVENT",
  SWIPER : "SWIPER",
  SWIPEE : "SWIPEE",
  IS_LEFT_SWIPE : "IS_LEFT_SWIPE"
});

//needs: attendanceID
Parse.Cloud.define("getSortedProfiles", function(request, response){
  var q = new Parse.Query(Attendance);
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
      response.success(array);
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
        var USER_ID = "user_id";
        var FIRST_NAME = "pr_first_name";
        var LAST_NAME = "pr_last_name";
        obj[USER_ID] = user.id;
        var first_name = user.get(FIRST_NAME);
        if (first_name === undefined){
          obj[FIRST_NAME] = user.get("name");
          obj[LAST_NAME] = "";
        } else {
          obj[FIRST_NAME] = first_name;
          obj[LAST_NAME] = user.get(LAST_NAME);
        }
        array.push(obj);
      });
      response.success(array);
    }
  }, function(error){
    response.error(error.message);
  });
});
