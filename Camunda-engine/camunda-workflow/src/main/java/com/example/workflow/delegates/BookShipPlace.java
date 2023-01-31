package com.example.workflow.delegates;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;

@Named
public class BookShipPlace implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        String money = "0.0";
        String ticketType = "None";

        money = (String) delegateExecution.getVariable("money");
        double moneyDouble = Double.parseDouble(money);

        if (moneyDouble >= 10000) {
            ticketType = "First Class";
        } else if (moneyDouble >= 5000) {
            ticketType = "Business Class";
        }
        else if(moneyDouble <=10){
            ticketType = "Stowaway Class";
            throw new BpmnError("FallOverboard","A cheap ticket has led to a small wave throwing you overboard .");
        }

        delegateExecution.setVariable("ticketType", ticketType);
    }
}