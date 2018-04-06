var stars = [];

function log(s) {
  console.log(s);
}

function createGalaxy() {
  log("createGalaxy");

  var armCount = 6;

  //for (var i = 0; i < 1000; i++) {
  while (stars.length < 1000) {
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
        y: y
      };
      stars.push(star);
    }
  }
}

function drawMap() {
  log("drawMap");
  var c = document.getElementById("universe");
  var ctx = c.getContext("2d");

  var width = c.width;
  var height = c.height;

  for (var i = 0; i < 1000; i++) {
    var star = stars[i];

    var x = width / 2 * (star.x + 1);
    var y = height / 2 * (star.y + 1);

    c.style.webkitFilter = "blur(1px)";
    ctx.beginPath();
    // TODO: make suns change size as time passes
    ctx.arc(x, y, 12, 0, 2.0 * Math.PI)
    // TODO: make suns change colour as time passes
    ctx.fillStyle = "rgb(255,255,128)";
    ctx.fill();
  }
}

function resizeMap() {
  var size = parseInt($('#map_size').val());
  $('#universe').width(size);
  $('#universe').height(size);

  drawMap();
}

$('#do_it').on('click', function(event) {
  $('#do_it').toggleClass('btn-primary');
});

$('#map_size').on('input change',function(event){
  resizeMap();
});

$(window).on('load', function(event) {
  createGalaxy();
  resizeMap();
});
