import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

import Modeler from 'bpmn-js/lib/Modeler.js';
import propertiesPanelModule from 'bpmn-js-properties-panel';
import propertiesProviderModule from 'bpmn-js-properties-panel/lib/provider/camunda';
import camundaModdleDescriptor from 'camunda-bpmn-moddle/resources/camunda.json';

import { saveAs } from 'file-saver';


@Component({
  selector: 'app-modeler',
  templateUrl: './modeler.component.html',
  styleUrls: ['./modeler.component.css']
})
export class ModelerComponent implements OnInit {
  title = 'Workflow Modeler';
  modeler: Modeler;

  @ViewChild('canvas')
  private canvesRef: ElementRef;

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.modeler = new Modeler({
      container: '#canvas',
      width: '100%',
      height: '500px',
      propertiesPanel: {
        parent: '#properties'
      },
      additionalModules: [
        propertiesPanelModule,
        propertiesProviderModule,
      ],
     
      moddleExtensions: {
        camunda: camundaModdleDescriptor
      }
    });
    this.load();
  }

  load(): void {
    this.getExample().subscribe(data => {
      this.modeler.importXML(data, (value: any) => this.handleError(value));
    });
  }

  handleError(err: any) {
    if (err) {
      console.warn('Ups, error: ', err);
    }
  }

  public getExample(): Observable<string> {
    var propertiesPanel = this.modeler.get('propertiesPanel');
    console.log(propertiesPanel);
    //console.log(template);
    
    const url = '/assets/bpmn/process.bpmn'; // local
    return this.http.get(url, {responseType: 'text'});
  }
  aveSVGFormat(){
    this.modeler.saveSVG((err: any, svg: any) => {
      const blob = new Blob([svg], {type: 'text/plain;charset=utf-8'});
      saveAs.saveAs(blob, 'bpmnSample.bpmn');
    });
  }
  save(): void {
    this.modeler.saveXML({ format: true }, (err: any, xml: any) => {
      console.log('Result of saving XML: ', xml);
      const blob = new Blob([xml], {type: 'text/plain;charset=utf-8'});
      saveAs.saveAs(blob, 'generatedWorkflow.bpmn');
    });
  }
  
}
