import { Component, OnInit } from '@angular/core';
//import { Form } from '@bpmn-io/form-js-viewer';
import { FormEditor } from '@bpmn-io/form-js-editor';
import { createFormEditor } from "@bpmn-io/form-js";

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.css']
})
export class FormComponent implements OnInit {

  constructor() { }

  async ngOnInit() {
    const schema = {
      "components": [
        {
          "type": "text",
          "text": "# flowvioo \nform __generator__ `form-js`.\n  \n  \nA list of BPMN symbols:\n* Start Event\n* Task\nLearn more about [forms](https://bpmn.io).\n  \n"
        },
        {
          "key": "creditor",
          "label": "Creditor",
          "type": "textfield",
          "validate": {
            "required": true
          }
        },
        {
          "description": "An invoice number in the format: C-123.",
          "key": "invoiceNumber",
          "label": "Invoice Number",
          "type": "textfield",
          "validate": {
            "pattern": "^C-[0-9]+$"
          }
        },
        {
          "key": "amount",
          "label": "Amount",
          "type": "number",
          "validate": {
            "min": 0,
            "max": 1000
          }
        },
        {
          "key": "approved",
          "label": "Approved",
          "type": "checkbox"
        },
        {
          "key": "approvedBy",
          "label": "Approved By",
          "type": "textfield",
          "conditional": {
            "hide": "=approved = false"
          }
        },
        {
          "key": "approverComments",
          "label": "Approver comments",
          "type": "textarea",
          "conditional": {
            "hide": "=approved = false"
          }
        },
        {
          "key": "supportPhoneNumber",
          "label": "Support Phone Number ",
          "type": "textfield",
          "validate": {
            "validationType": "phone"
          }
        },
        {
          "key": "mailto",
          "label": "Email data to",
          "type": "checklist",
          "values": [
            {
              "label": "Approver",
              "value": "approver"
            },
            {
              "label": "Manager",
              "value": "manager"
            },
            {
              "label": "Technical Leader",
              "value": "technical-leader"
            }
          ]
        },
        {
          "key": "product",
          "label": "Product",
          "type": "radio",
          "values": [
            {
              "label": "Camunda Platform",
              "value": "camunda-platform"
            },
            {
              "label": "Camunda Cloud",
              "value": "camunda-cloud"
            }
          ]
        },
        {
          "key": "dri",
          "label": "Assign DRI",
          "type": "radio",
          "valuesKey": "queriedDRIs"
        },
        {
          "key": "tags",
          "label": "Taglist",
          "type": "taglist",
          "values": [
            {
              "label": "Tag1",
              "value": "tag1"
            },
            {
              "label": "Tag2",
              "value": "tag2"
            },
            {
              "label": "Tag3",
              "value": "tag3"
            }
          ]
        },
        {
          "key": "language",
          "label": "Language",
          "type": "select",
          "values": [
            {
              "label": "German",
              "value": "german"
            },
            {
              "label": "English",
              "value": "english"
            }
          ]
        },
        {
          "key": "conversation",
          "type": "datetime",
          "subtype": "datetime",
          "dateLabel": "Date of conversation",
          "timeLabel": "Time of conversation",
          "timeSerializingFormat": "utc_normalized",
          "timeInterval": 15,
          "use24h": false
        },
        {
          "source": "=logo",
          "alt": "The bpmn.io logo",
          "type": "image"
        },
        {
          "key": "submit",
          "label": "Submit",
          "type": "button"
        },
        {
          "action": "reset",
          "key": "reset",
          "label": "Reset",
          "type": "button"
        }
      ],
      "type": "default"
    };

    const data = {
      creditor: 'WEVIOO Se Team',
      invoiceNumber: 'C-2002',
      amount: 250,
    };

    //const formContainer = document.querySelector('#form');
    const formEditorContainer = document.querySelector('#canvas');
    
    const formEditor = await createFormEditor({
      container: formEditorContainer ,
      schema,
      exporter: {
        name: "flowvioo form editor",
        version: "1.0.0"
      }
    });



   /* const form = new Form({
      container: formContainer
    });*/
    
    const editor = new FormEditor({
      container: formEditorContainer
    });

    //await form.importSchema(schema, data);

    /*add event listeners
    form.on('submit', (event: any) => {
      console.log('Form <submit>', event);
    });

    // provide a priority to event listeners
    form.on('changed', 500, (event: any) => {
      console.log('Form <changed>', event);
    });*/ 


  }
}