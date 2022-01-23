package ru.itmo.robq.web4.network;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class MessageResponse implements Serializable {

    private String message;

    private static final long serialVersionUID = 5306208306185377637L;

}
