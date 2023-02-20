import { Component, ElementRef, ViewChild ,OnInit } from '@angular/core';
import BpmnModeler from 'bpmn-js/lib/Modeler';

@Component({
  selector: 'app-modeler',
  templateUrl: "./modeler.component.html",
  styleUrls: ['./modeler.component.css']
})
export class ModelerComponent implements OnInit {
  @ViewChild('canvas', { static: true }) canvasRef: ElementRef;

  bpmnModeler :BpmnModeler
  constructor() { }

  ngOnInit() {
    
    this.bpmnModeler = new BpmnModeler({
      container: this.canvasRef.nativeElement
    });
    
      //bpmnModeler.createDiagram();
      // Load BPMN file
      const url = './assets/bpmn/process.bpmn';
      const xml = new XMLHttpRequest();
      xml.open('GET', url, true);
      xml.onreadystatechange = () => {
        if (xml.readyState === 4 && xml.status === 200) {
          this.bpmnModeler.importXML(xml.responseText, (err: any) => {
            if (err) {
              console.error(err);
            } else {
              const canvas = this.bpmnModeler.get('canvas');
              const viewbox = canvas.viewbox();
              const zoomFactor = Math.min(
                canvas._container.clientWidth / viewbox.outer.width,
                canvas._container.clientHeight / viewbox.outer.height,
                1
              ) * 0.8; // adjust zoom factor here
              canvas.zoom(zoomFactor, { x: canvas._container.clientWidth/6, y: canvas._container.clientHeight/2 });
            }
          });
        }
      };
      xml.send();
  }

  ngOnDestroy() {
    this.bpmnModeler.destroy();
  }
}
