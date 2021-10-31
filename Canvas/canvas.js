var color = '#00d5ff'
var selected_color = '#f05800'
var close_color = '#8aff78'
var main_color = '#f5f5f5'
var close_circles = []
var scale = 20
var mustDrawGrid = false
var mustDrawRc = false
var mustColor = false
var drawArrows = false
var paused = false
class Circle {
    constructor(id, x, y, r) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.r = r;
    }

    draw() {
        c.beginPath();
        c.arc(this.x, this.y, this.r, 0, Math.PI * 2, false);
        c.fillStyle = selected_color; 
        c.fill();

        c.font = `${this.r * 0.7}pt Calibri`;
        c.fillStyle = '#000000';
        c.textAlign = 'center';
        c.fillText(this.id, this.x, this.y+3);
    }

    update(x, y, r) {
        this.x = x
        this.y = y
        this.r = r
        this.draw();
    }
}

//---------------Set up Canvas----------------

var canvas = document.querySelector('canvas');
canvas.width = window.innerWidth;
canvas.height = window.innerHeight;
var c = canvas.getContext('2d'); 

//----------------buttons-----------------------

document.getElementById('plus-btn').addEventListener("click", function() {
    framerate = framerate * 2;
    frameSize =  1/framerate;
    init();
});

document.getElementById('minus-btn').addEventListener("click", function() {
    framerate = framerate / 2;
    frameSize =  1/framerate;
    init();
});

document.getElementById('restart_sim_btn').addEventListener("click", function() {
    init()
});

document.getElementById('toggle_pause_btn').addEventListener("click", function() {
    paused = !paused
});

document.getElementById('submit_scale_btn').addEventListener('click', function() {
    scale = document.getElementById('scale_input').value
    init()
});

//-------------draw circles---------------------

let simulation = []

document.getElementById('inputfile')
    .addEventListener('change', function() {
        var fr=new FileReader();
        fr.onload=function(){
            simulation = JSON.parse(fr.result);
            init()
        }
        fr.readAsText(this.files[0]);
    })

var circleArray = [];

let frames = 1
let curr_frame = 0
let requestID = null
let framerate = 10000
let frameSize =  1/framerate
let time = 0
let lastTime = time
let valid = true;

function init(){

    document.getElementById('speed-input').value = framerate;
    
    if(requestID){
        window.cancelAnimationFrame(requestID);
    }
    
    console.log(simulation)

    circleArray = [];

    frames = simulation.events.length-1
    curr_frame = 0

    canvas.width = simulation.Lx * scale;
    canvas.height = simulation.Ly * scale;

    drawBorders()

    let i = 0
    for (circle of simulation.events[curr_frame].circles){
        circleArray.push(new Circle(i, circle.x * scale, circle.y * scale, circle.r * scale));
        i++;
    }

    for(circle of circleArray){
        circle.draw()
    }

    if(frames > 1)
        animate();
}

function animate(){
    c.clearRect(0,0,simulation.Lx * scale, simulation.Ly * scale);
    drawBorders()

    if(paused){
        for(let j=0; j<simulation.events[curr_frame].circles.length;j++){
            let currCircle = simulation.events[curr_frame].circles[j]
            circleArray[j].update(currCircle.x * scale, currCircle.y * scale, currCircle.r * scale)
        }
        requestID = requestAnimationFrame(animate);
    } else{
        while(time < lastTime + frameSize){
            if(curr_frame < simulation.events.length-1){
                time += simulation.events[curr_frame].t
                curr_frame++
                frames--
            }else{
                valid = false
                break
            }
        }
        lastTime = time
    
        if(valid){
            for(let j=0; j<simulation.events[curr_frame].circles.length;j++){
                let currCircle = simulation.events[curr_frame].circles[j]
                circleArray[j].update(currCircle.x * scale, currCircle.y * scale, currCircle.r * scale)
            }
        }
    
        console.log('Time: ', time)
    
    
        if(frames > 0){
            requestID = requestAnimationFrame(animate);
        }else{
            frames = simulation.events.length-1;
            curr_frame = 0;
            time = 0
            lastTime = 0
            requestID =requestAnimationFrame(animate);
            valid = true
        }
    }
}

function drawBorders(){
    c.beginPath();
    c.moveTo(0, 0);
    c.lineTo(0, simulation.Ly * scale);
    c.lineTo(simulation.Lx * scale, simulation.Ly * scale);
    c.lineTo(simulation.Lx * scale, 0);
    c.lineTo(0, 0);

    var wallSize = simulation.Lx* (1-simulation.gap)/2
    var gapSize =  simulation.gap * simulation.Lx
    console.log(simulation.Lx + simulation.rmax)
    c.moveTo((simulation.Ly/ 2) * scale, 0);
    c.lineTo((simulation.Ly/ 2) * scale , (wallSize) * scale );
    c.stroke();

    c.moveTo((simulation.Ly/ 2) * scale, (simulation.Lx) * scale);
    c.lineTo((simulation.Ly/ 2) * scale, (wallSize+ gapSize) * scale );
    c.stroke();

    c.strokeStyle = "#FFFFFF"
    c.stroke();
}