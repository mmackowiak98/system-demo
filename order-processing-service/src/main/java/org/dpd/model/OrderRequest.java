package org.dpd.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderRequest {
    @NotNull
    private String shipmentNumber;
    @NotNull
    private String receiverEmail;
    @NotNull
    private String receiverCountryCode;
    @NotNull
    private String senderCountryCode;
    @NotNull
    @Min(0)
    @Max(100)
    private int statusCode;
}