package com.model;

import lombok.*;

@Getter
@AllArgsConstructor
public class Host {
    private String name, host, service;
    private int port;
}
