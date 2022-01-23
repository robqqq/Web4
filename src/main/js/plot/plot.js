import store from "../store/store";
import {check} from "../api/request";

export function drawPlot(canvas) {
    const width = canvas.width;
    const center = width / 2;
    const r = store.getState().r * width / 5;
    const ctx = canvas.getContext("2d");
    ctx.clearRect(0, 0, width, width);
    ctx.fillStyle = "rgba(48, 63, 159, 0.5)";
    ctx.strokeStyle = "rgb(48, 63, 159)";
    ctx.beginPath();
    ctx.moveTo(center, center);
    ctx.lineTo(center, center + r / 2);
    ctx.lineTo(center - r, center);
    ctx.arc(center, center, r, Math.PI, 3 * Math.PI / 2, );
    ctx.lineTo(center, center - r / 2);
    ctx.lineTo(center + r, center - r / 2);
    ctx.lineTo(center + r, center);
    ctx.lineTo(center, center);
    ctx.fill();
    ctx.stroke();
    drawGrid(canvas);
    drawPoints(canvas);
}

function drawLine(ctx, begin, end, stroke = 'black', width = 1) {
    if (stroke) {
        ctx.strokeStyle = stroke;
    }
    if (width) {
        ctx.lineWidth = width;
    }
    ctx.beginPath();
    ctx.moveTo(...begin);
    ctx.lineTo(...end);
    ctx.stroke();
}

function drawPoints(canvas) {
    let points = store.getState().points;
    let drawnPoints = new Set();
    for (const point of points) {
        if (!drawnPoints.has(point.x.toString() + point.y.toString() + point.r.toString() + point.result.toString())) {
            drawPoint(canvas, point);
            drawnPoints.add(point.x.toString() + point.y.toString() + point.r.toString() + point.result.toString());
        }
    }
}

function drawPoint(canvas, point) {
    const ctx = canvas.getContext('2d');
    const width = canvas.width;
    const r = width / 5;
    const cr = width / 100;
    const center = width / 2;
    if (point.result) {
        ctx.fillStyle = "rgba(104, 159, 56, 0.5)";
        ctx.strokeStyle = "rgb(104, 159, 56)";
    } else {
        ctx.fillStyle = "rgba(211, 47, 47, 0.5)";
        ctx.strokeStyle = "rgb(211, 47, 47)";
    }
    ctx.beginPath();
    ctx.arc(center + point.x * r, center - point.y * r, cr, 0, 2 * Math.PI);
    ctx.fill();
    ctx.stroke();
}

function drawGrid(canvas) {
    const width = canvas.width;
    const center = width / 2;
    const r = width / 5;
    const ctx = canvas.getContext('2d');
    drawLine(ctx, [r / 4, center], [width - r / 4, center]);
    drawLine(ctx, [center, r / 4], [center, width - r / 4]);
    drawLine(ctx, [center - r / 20, r / 4 + r / 8], [center, r / 4]);
    drawLine(ctx, [center, r / 4], [center + r / 20, r / 4 + r / 8]);
    drawLine(ctx, [width - r / 4 - r / 8, center - r / 20], [width - r / 4, center]);
    drawLine(ctx, [width - r / 4, center], [width - r / 4 - r / 8, center + r / 20]);
    drawLine(ctx, [center - 2 * r, center - r / 20], [center - 2 * r, center + r / 20]);
    drawLine(ctx, [center - r, center - r / 20], [center - r, center + r / 20]);
    drawLine(ctx, [center + r, center - r / 20], [center + r, center + r / 20]);
    drawLine(ctx, [center + 2 * r, center - r / 20], [center + 2 * r, center + r / 20]);
    drawLine(ctx, [center - r / 20, center - 2 * r], [center + r / 20, center - 2 * r]);
    drawLine(ctx, [center - r / 20, center - r], [center + r / 20, center - r]);
    drawLine(ctx, [center - r / 20, center + r], [center + r / 20, center + r]);
    drawLine(ctx, [center - r / 20, center + 2 * r], [center + r / 20, center + 2 * r]);
}

export function clicked(e) {
    const r = store.getState().r
    if (r === "" || r === null) {
        return "R is not set";
    }
    if (isNaN(r)){
        return "R must be a number";
    }
    if (r <= 0 || r > 2 ) {
        return "R value must be in (0; 2]";
    }
    const canvas = document.getElementById('canvas');
    const rect = canvas.getBoundingClientRect();
    const width = canvas.width;
    const center = width / 2;
    const dr = width / 5;
    const clickX = e.clientX - rect.left;
    const clickY = e.clientY - rect. top;
    let x = (clickX - center) / dr;
    let y = (center - clickY) / dr;
    if (x < -2 || x > 2) {
        return "X value must be in [-2; 2]";
    }
    if (y <= -5 || y >= 5) {
        return "Y value must be in (-5; 5)";
    }
    check({x: x, y: y, r: r}).then(response => response.json().then(json => {
        if (response.ok) {
            store.dispatch({type: "appendPoint", value: json});
            drawPlot(document.getElementById('canvas'));
        } else {
            return json.message;
        }
    }));
    return "";
}
