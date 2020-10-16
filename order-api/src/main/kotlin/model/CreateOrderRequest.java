package model;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.romanow.services.order.model.enums.SizeChart;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class CreateOrderRequest {

    @NotEmpty(message = "{field.is.empty")
    private String model;

    @NotNull(message = "{field.is.null")
    private SizeChart size;
}
