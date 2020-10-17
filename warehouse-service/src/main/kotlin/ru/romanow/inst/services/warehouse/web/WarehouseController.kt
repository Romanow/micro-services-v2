package ru.romanow.inst.services.warehouse.web

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ru.romanow.inst.services.warehouse.model.ItemInfoResponse
import ru.romanow.inst.services.warehouse.model.OrderItemRequest
import ru.romanow.inst.services.warehouse.model.OrderItemResponse
import ru.romanow.inst.services.warehouse.service.WarehouseService
import ru.romanow.inst.services.warehouse.service.WarrantyService
import ru.romanow.inst.services.warranty.model.OrderWarrantyRequest
import ru.romanow.inst.services.warranty.model.OrderWarrantyResponse
import java.util.*
import javax.validation.Valid

@Tag(name = "Warehouse API")
@RestController
@RequestMapping("/api/v1/warehouse")
class WarehouseController(
    private val warehouseService: WarehouseService,
    private val warrantyService: WarrantyService
) {

    @Operation(summary = "Get item information")
    @ApiResponse(responseCode = "200", description = "Item information")
    @GetMapping(value = ["/{itemId}"], produces = ["application/json"])
    private fun item(@PathVariable itemId: UUID): ItemInfoResponse {
        return warehouseService.getItemInfo(itemId)
    }

    @Operation(summary = "Take item from warehouse")
    @ApiResponse(responseCode = "200", description = "Information about item in order")
    @PostMapping(consumes = ["application/json"], produces = ["application/json"])
    fun takeItem(@Valid @RequestBody request: OrderItemRequest): OrderItemResponse {
        return warehouseService.takeItem(request)
    }

    @Operation(summary = "Return item")
    @ApiResponse(responseCode = "204", description = "Return item")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{orderItemUid}")
    fun returnItem(@PathVariable orderItemUid: UUID) {
        warehouseService.returnItem(orderItemUid)
    }

    @Operation(summary = "Request item warranty")
    @ApiResponse(responseCode = "204", description = "Warranty decision")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/{orderItemUid}/warranty")
    fun warranty(@PathVariable orderItemUid: UUID, @Valid @RequestBody request: OrderWarrantyRequest): OrderWarrantyResponse {
        return warrantyService.warrantyRequest(orderItemUid, request)
    }
}
