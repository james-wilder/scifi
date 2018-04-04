function drawMap() {
  console.log("drawMap")
  var c = document.getElementById("universe");
  var ctx = c.getContext("2d");

  for (var i = 0; i < 1000; i++) {
    ctx.beginPath();
    ctx.arc(200,100,20,0*Math.PI,2.0*Math.PI)
    ctx.fillStyle = "white";
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
  resizeMap();
});
