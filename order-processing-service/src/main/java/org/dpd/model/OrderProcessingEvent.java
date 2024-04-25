package org.dpd.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class OrderProcessingEvent extends ApplicationEvent {
    private transient OrderRequest order;

    public OrderProcessingEvent(Object source, OrderRequest order) {
        super(source);
        this.order = order;
    }
}
