package org.dpd.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class OrderLogResponse {
    private String shipmentNumber;
    private String receiverEmail;
    private String receiverCountryCode;
    private String senderCountryCode;
    private int statusCode;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
