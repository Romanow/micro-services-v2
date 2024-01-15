package ru.romanow.inst.services.warehouse.web

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ru.romanow.inst.services.common.model.ErrorResponse
import ru.romanow.inst.services.warehouse.model.ItemInfoResponse
import ru.romanow.inst.services.warehouse.model.OrderItemRequest
import ru.romanow.inst.services.warehouse.model.OrderItemResponse
import ru.romanow.inst.services.warehouse.service.WarehouseService
import ru.romanow.inst.services.warehouse.service.WarrantyService
import ru.romanow.inst.services.warranty.model.OrderWarrantyRequest
import ru.romanow.inst.services.warranty.model.OrderWarrantyResponse
import java.util.UUID
import javax.validation.Valid

@Tag(name = "Warehouse API")
@RestController
@RequestMapping("/api/v1/warehouse")
class WarehouseController(
    private val warehouseService: WarehouseService,
    private val warrantyService: WarrantyService
) {

    @Operation(summary = "Get item information")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Item information"),
            ApiResponse(
                responseCode = "404",
                description = "Item info not found",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @GetMapping(value = ["/{orderItemUid}"], produces = ["application/json"])
    private fun item(@PathVariable orderItemUid: UUID): ItemInfoResponse {
        return warehouseService.getItemInfo(orderItemUid)
    }

    @Operation(summary = "Take item from warehouse")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Item add to order"),
            ApiResponse(
                responseCode = "400",
                description = "Bad request format",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Requested item not found",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "409",
                description = "Item not available",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PostMapping(consumes = ["application/json"], produces = ["application/json"])
    fun takeItem(
        @Valid @RequestBody request: OrderItemRequest
    ): OrderItemResponse {
        return warehouseService.takeItem(request)
    }

    @Operation(summary = "Return item")
    @ApiResponse(responseCode = "204", description = "Item returned")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{orderItemUid}")
    fun returnItem(@PathVariable orderItemUid: UUID) {
        warehouseService.returnItem(orderItemUid)
    }

    @Operation(summary = "Request item warranty")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Warranty decision"),
            ApiResponse(
                responseCode = "400",
                description = "Bad request format",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Requested item not found",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "422",
                description = "External request failed",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PostMapping("/{orderItemUid}/warranty")
    fun warranty(
        @PathVariable orderItemUid: UUID,
        @Valid @RequestBody request: OrderWarrantyRequest
    ): OrderWarrantyResponse {
        return warrantyService.warrantyRequest(orderItemUid, request)
    }
}
