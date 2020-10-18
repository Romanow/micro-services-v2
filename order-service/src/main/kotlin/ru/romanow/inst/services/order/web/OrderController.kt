package ru.romanow.inst.services.order.web

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ru.romanow.inst.services.order.model.CreateOrderRequest
import ru.romanow.inst.services.order.model.ErrorResponse
import ru.romanow.inst.services.order.model.OrderInfoResponse
import ru.romanow.inst.services.order.model.OrdersInfoResponse
import ru.romanow.inst.services.order.service.OrderManagementService
import ru.romanow.inst.services.order.service.OrderService
import ru.romanow.inst.services.warranty.model.OrderWarrantyRequest
import ru.romanow.inst.services.warranty.model.OrderWarrantyResponse
import java.util.*
import javax.validation.Valid

@Tag(name = "Order API")
@RestController
@RequestMapping("/api/v1/orders")
class OrderController(
    private val orderService: OrderService,
    private val orderManagementService: OrderManagementService
) {

    @Operation(summary = "User order info")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Order info"),
        ApiResponse(responseCode = "404", description = "Order not found", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    ])
    @GetMapping("/{userUid}/{orderUid}", produces = ["application/json"])
    fun userOrder(@PathVariable userUid: UUID, @PathVariable orderUid: UUID): OrderInfoResponse {
        return orderService.getUserOrder(userUid, orderUid)
    }

    @Operation(summary = "User orders info")
    @ApiResponse(responseCode = "200", description = "Orders info")
    @GetMapping("/{userUid}", produces = ["application/json"])
    fun userOrders(@PathVariable userUid: UUID): OrdersInfoResponse {
        return orderService.getUserOrders(userUid)
    }

    @Operation(summary = "Create order")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Order created"),
        ApiResponse(responseCode = "400", description = "Bad request format", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "409", description = "Item not available", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "422", description = "External request failed", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    ])
    @PostMapping(value = ["/{userUid}"], consumes = ["application/json"], produces = ["application/json"])
    fun makeOrder(@PathVariable userUid: UUID, @Valid @RequestBody request: CreateOrderRequest): UUID {
        return orderManagementService.makeOrder(userUid, request)
    }

    @Operation(summary = "Return order")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Order returned"),
        ApiResponse(responseCode = "404", description = "Order not found", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "422", description = "External request failed", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    ])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = ["/{orderUid}"])
    private fun refundOrder(@PathVariable orderUid: UUID) {
        orderManagementService.refundOrder(orderUid)
    }

    @Operation(summary = "Warranty request")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Warranty decision"),
        ApiResponse(responseCode = "400", description = "Bad request format", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "404", description = "Order not found", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "422", description = "External request failed", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    ])
    @PostMapping(value = ["/{orderUid}/warranty"], consumes = ["application/json"], produces = ["application/json"])
    private fun warranty(@PathVariable orderUid: UUID,
                         @Valid @RequestBody request: OrderWarrantyRequest): OrderWarrantyResponse {
        return orderManagementService.useWarranty(orderUid, request)
    }
}