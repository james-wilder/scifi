var galaxyEnd = 10000000000; // 10 billion
var starCount = 500;
var armCount = 6;
var timelineAt = 0;

var stars = [];
var events = [];

function log(s) {
  console.log(s);
}

function createGalaxy() {
  log("createGalaxy");

  while (stars.length < starCount) {
    var arm = Math.floor(Math.random() * (armCount + 1));
    var d = Math.random();
    var a = (arm / armCount) * 2.0 * Math.PI + d * 2;

    var x = Math.sin(a) * d * 800;
    var y = Math.cos(a) * d * 800;

    for (var j = 0; j < 3; j++) {
      x = x + (1 - d) * 100 * (Math.random() - 0.5);
      y = y + (1 - d) * 100 * (Math.random() - 0.5);
    }

    x = x + 2000 * (Math.random() - 0.5) * (Math.random() - 0.5) * (Math.random() - 0.5);
    y = y + 2000 * (Math.random() - 0.5) * (Math.random() - 0.5) * (Math.random() - 0.5);

    x = x / 1000;
    y = y / 1000;

    if ((x > -1) && (x < 1) && (y > -1) && (y < 1)) {
      var r = Math.floor(Math.random() * 256);
      var g = Math.floor(Math.random() * 256);
      var b = Math.floor(Math.random() * 256);
      var star = {
        x: x,
        y: y,
        lifeformWillEvolveAt: Math.random() * Math.random() * 100000000000,
        population: 0,
        lifeformColor: 'rgb(' + r + ',' + g + ',' + b + ')',
      };
      stars.push(star);
    }
  }
}

function createEventList() {
  log("createEventList");

  var galaxyState = {
    now: 0,
    populations: [],
    lifeforms: [],
  }

  var event = {
    at: 0,
    type: "start"
  }
  events.push(event);
  while (true) {
    var event = getNextEvent(galaxyState);
    log(event.type);
    events.push(event);

    processEvent(galaxyState, event);

    galaxyState.now = event.at;

    if (event.type == "end") {
      break;
    }
  }

  log("createEventList finished");
}

function getNextEvent(galaxyState) {
  log("getNextEvent");
  var nextEvent = {
    at: galaxyEnd,
    type: "end"
  };

  // new lifeforms
  for (var i = 0; i < stars.length; i++) {
    var star = stars[i];
    if (star.lifeformWillEvolveAt >= galaxyState.now) {
      if (getPopulationsAt(galaxyState, star).length == 0) {
        if (star.lifeformWillEvolveAt < nextEvent.at) {
          nextEvent = {
            at: star.lifeformWillEvolveAt,
            type: "new_lifeform",
            star: star,
            lifeform: {
              name: 'at_' + star.lifeformWillEvolveAt,  // TODO: name them
              color: star.lifeformColor
            },
          };
        }
      }
    }
  }

  // colonization
  for (var s = 0; s < stars.length; s++) {
    var star = stars[s];
    if (getPopulationsAt(galaxyState, star).length > 0) {
      continue;
    }

    for (var l = 0; l < galaxyState.lifeforms.length; l++) {
      var lifeform = galaxyState.lifeforms[l];

      // TODO: variability
      var techLevelRate = 0.00001;
      var colonizeAbility = 1 + galaxyState.now * techLevelRate;

      var populations = getLifeformPopulations(galaxyState, lifeform);
      for (var p = 0; p < populations.length; p++) {
        var population = populations[p];
        if (population.size < 10) {
          continue;
        }

        var distance = getDistanceSquared(population.location, star);
        var techLevelToColonize = distance * 10000000;

        var timeToColonize = (techLevelToColonize - colonizeAbility) / techLevelRate;

        if (galaxyState.now + timeToColonize < nextEvent.at) {
          nextEvent = {
            at: galaxyState.now + timeToColonize,
            type: "colonization",
            star: star,
            lifeform: lifeform,
          };
        }
      }
    }
  }

  return nextEvent;
}

function getDistanceSquared(loc1, loc2) {
  var xd = loc1.x - loc2.x;
  var yd = loc1.y - loc2.y;
  return xd * xd + yd * yd;
}

function getGalaxyState(events, at) {
  log("getGalaxyState");

  var galaxyState = {
    now: 0,
    populations: [],
    lifeforms: []
  }

  for (var i = 0; i < events.length; i++) {
    var event = events[i];

    if (event.at > at) {
      galaxyState.now = at;
      break;
    }

    processEvent(galaxyState, event);

    if (event.type == "end") {
      break;
    }
  }

  log("getGalaxyState finished");

  return galaxyState;
}

function processEvent(galaxyState, event) {
  if (event.type == "new_lifeform") {
    population = {
      lifeform: event.lifeform,
      location: event.star,
      size: 10,
    };
    galaxyState.populations.push(population);

    galaxyState.lifeforms.push(event.lifeform);
  }

  if (event.type == "colonization") {
    population = {
      lifeform: event.lifeform,
      location: event.star,
      size: 1,
    };
    galaxyState.populations.push(population);
  }
}

function getPopulationsAt(galaxyState, location) {
  var populations = [];
  for (var i = 0; i < galaxyState.populations.length; i++) {
    var population = galaxyState.populations[i];
    if (population.location == location) {
      populations.push(population);
    }
  }
  return populations;
}

function getLifeformPopulations(galaxyState, lifeform) {
  var populations = [];
  for (var i = 0; i < galaxyState.populations.length; i++) {
    var population = galaxyState.populations[i];
    if (population.lifeform == lifeform) {
      populations.push(population);
    }
  }
  return populations;
}

function drawMap() {
  log("drawMap");

  var galaxyState = getGalaxyState(events, timelineAt);

  var c = document.getElementById("universe");
  var ctx = c.getContext("2d");
  ctx.clearRect(0, 0, c.width, c.height);

  var width = c.width;
  var height = c.height;

  for (var i = 0; i < stars.length; i++) {
    var star = stars[i];

    var x = width / 2 * (star.x + 1);
    var y = height / 2 * (star.y + 1);

    c.style.webkitFilter = "blur(1px)";
    ctx.beginPath();
    
    // TODO: make suns change size as time passes
    ctx.arc(x, y, 12, 0, 2.0 * Math.PI)

    // TODO: make suns change colour as time passes
    ctx.fillStyle = "rgb(255,255,128)";
    
    // TODO: different color for each lifeform
    var populations = getPopulationsAt(galaxyState, star);
    if (populations.length == 0) {
      ctx.fill();
    }
  }

  for (var i = 0; i < stars.length; i++) {
    var star = stars[i];

    var x = width / 2 * (star.x + 1);
    var y = height / 2 * (star.y + 1);

    c.style.webkitFilter = "blur(1px)";
    ctx.beginPath();
    
    // TODO: make suns change size as time passes
    ctx.arc(x, y, 12, 0, 2.0 * Math.PI)

    // TODO: make suns change colour as time passes
    ctx.fillStyle = "rgb(255,255,128)";
    
    // TODO: different color for each lifeform
    var populations = getPopulationsAt(galaxyState, star);
    if (populations.length > 0) {
      ctx.fillStyle = populations[0].lifeform.color;
    }
    
    ctx.fill();
  }
}

function resizeMap() {
  var size = parseInt($('#map_size').val());
  $('#universe').width(size);
  $('#universe').height(size);

  drawMap();
}

function outputEventsToUI() {
  log("outputEventsToUI");

  for (var i = 0; i < events.length; i++) {
    var event = events[i];
    log(event.at + ": " + event.type);
    $('#event_list').append('<div class="event" data-at="' + event.at + '">' 
      + event.at + ': ' + event.type + '</div>');
  }
  $('.event').on('click', function(event) {
  	setTimeline($(this).data('at'));
  });
}

function setTimeline(now) {
  log(now);

  var c = document.getElementById("timeline");
  var ctx = c.getContext("2d");
  ctx.clearRect(0, 0, c.width, c.height);

  for (var i = 0; i < events.length; i++) {
    markTimeline(events[i].at, '#800');
  }
  markTimeline(now, 'white');

  timelineAt = now;

  drawMap();
}

function markTimeline(at, color) {
  var c = document.getElementById("timeline");
  var ctx = c.getContext("2d");

  var width = c.width;
  var height = c.height;

  var x = width * at / galaxyEnd;

  ctx.beginPath();
  ctx.moveTo(x, 0);
  ctx.lineTo(x, height);
  ctx.strokeStyle = color;
  ctx.stroke();
}

$('#do_it').on('click', function(event) {
  $('#do_it').toggleClass('btn-primary');
});

$('#timeline').on('click', function(event) {
  var c = document.getElementById("timeline");
  var ctx = c.getContext("2d");

  setTimeline(galaxyEnd * event.pageX / c.width);
});

$('#map_size').on('input change',function(event){
  resizeMap();
});

$(window).on('load', function(event) {
  createGalaxy();
  createEventList();
  outputEventsToUI();
  resizeMap();
  setTimeline(0);
});
