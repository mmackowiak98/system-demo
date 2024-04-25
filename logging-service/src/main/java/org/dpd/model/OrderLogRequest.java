package org.dpd.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderLogRequest {
    private String shipmentNumber;
    private String receiverEmail;
    private String receiverCountryCode;
    private String senderCountryCode;
    private int statusCode;
}
