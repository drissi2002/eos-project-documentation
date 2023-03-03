package com.example.workflow.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProcessInstance {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String processInstanceId;
    private String processInstanceKey;
    @ManyToOne(fetch = FetchType.LAZY)
    private Workflow workflow;
    private String status;
}

