var Attendance  = Parse.Object.extend("Attendance",{
  getUser: function () {
    return this.get(Attendance.MAC);
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
