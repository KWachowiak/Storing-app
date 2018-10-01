package com.klaudiusz_wachowiak.storingapp.model;


import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class ResourceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Type(type = "text")
    private String name;

    private String description;

    @NotNull
    private String originUrlAddress;

    private String fileName;

    @NotNull
    @Lob
    private byte[] content;

    @NotNull
    private String contentType;
}
