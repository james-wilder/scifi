var galaxyEnd = 10000000000; // 10 billion
var starCount = 1000;
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
      var star = {
        x: x,
        y: y,
        lifeformWillEvolveAt: Math.random() * Math.random() * 100000000000,
        population: 0,
      };
      stars.push(star);
    }
  }
}

function createEventList() {
  log("createEventList");

  var galaxyState = {
    now: 0,
    events: [],
    populations: []
  }

  var event = {
    at: 0,
    type: "start"
  }
  galaxyState.events.push(event);
  while (true) {
    var event = getNextEvent(galaxyState);
    galaxyState.events.push(event);

    if (event.type == "new_lifeform") {
      population = {
        lifeform: event.lifeform,
        location: event.star,
        size: 1
      };
      galaxyState.populations.push(population);
    }

    if (event.type == "end") {
      break;
    }
  }

  log("createEventList finished");

  return galaxyState.events;
}

function getNextEvent(galaxyState) {
  var nextEvent = {
    at: galaxyEnd,
    type: "end"
  };
  for (var i = 0; i < stars.length; i++) {
    var star = stars[i];
    if (getPopulations(galaxyState, star) == 0) {
      if (star.lifeformWillEvolveAt >= galaxyState.now) {
        if (star.lifeformWillEvolveAt < nextEvent.at) {
          nextEvent = {
            at: star.lifeformWillEvolveAt,
            type: "new_lifeform",
            star: star,
            lifeform: 'at_' + star.lifeformWillEvolveAt
          };
        }
      }
    }
  }

  return nextEvent;
}

function getGalaxyState(events, at) {
  log("createEventList");

  var galaxyState = {
    now: 0,
    events: [],
    populations: []
  }

  var event = {
    at: 0,
    type: "start"
  }
  galaxyState.events.push(event);
  while (true) {
    var event = getNextEvent(galaxyState);
    galaxyState.events.push(event);

    if (event.at > at) {
      galaxyState.now = at;
      break;
    }

    if (event.type == "new_lifeform") {
      population = {
        lifeform: event.lifeform,
        location: event.star,
        size: 1
      };
      galaxyState.populations.push(population);
    }

    if (event.type == "end") {
      break;
    }
  }

  log("createEventList finished");

  return galaxyState;
}

function getPopulations(galaxyState, star) {
  var total = 0;
  for (var i = 0; i < galaxyState.populations.length; i++) {
    var population = galaxyState.populations[i];
    if (population.location == star) {
      total= total + population.size;
    }
  }
  return total;
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
    if (getPopulations(galaxyState, star) > 0) {
      ctx.fillStyle = "rgb(0,128,0)";
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
  events = createEventList();
  outputEventsToUI();
  resizeMap();
  setTimeline(0);
});
