package model;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.romanow.services.order.model.enums.PaymentStatus;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class OrderInfoResponse {
    private UUID orderId;
    private String orderDate;
    private UUID itemId;
    private PaymentStatus status;
}
