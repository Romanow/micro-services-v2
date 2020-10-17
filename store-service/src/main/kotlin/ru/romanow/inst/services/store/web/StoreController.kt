package ru.romanow.inst.services.store.web

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import ru.romanow.inst.services.store.model.*
import ru.romanow.inst.services.store.service.StoreService
import java.util.*
import javax.validation.Valid

@Tag(name = "Store API")
@RestController
@RequestMapping("/api/v1/store")
class StoreController(
    private val storeService: StoreService
) {

    @Operation(summary = "List user orders")
    @ApiResponse(responseCode = "200", description = "User orders")
    @GetMapping("/{userId}/orders", produces = ["application/json"])
    fun orders(@PathVariable userId: UUID): UserOrdersResponse {
        return storeService.findUserOrders(userId)
    }

    @Operation(summary = "User order info")
    @ApiResponse(responseCode = "200", description = "Order info")
    @GetMapping("/{userId}/{orderId}", produces = ["application/json"])
    fun orders(@PathVariable userId: UUID, @PathVariable orderId: UUID): UserOrderResponse {
        return storeService.findUserOrder(userId, orderId)
    }

    @Operation(summary = "Purchase items")
    @ApiResponse(responseCode = "201", description = "Item purchased")
    @PostMapping("/{userId}/purchase", consumes = ["application/json"])
    fun purchase(@PathVariable userId: UUID, @Valid @RequestBody request: PurchaseRequest): ResponseEntity<Void> {
        val orderId: UUID = storeService.makePurchase(userId, request)
        val uri = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{orderId}")
            .buildAndExpand(orderId)
            .toUri();

        return ResponseEntity.created(uri).build()
    }

    @Operation(summary = "Return items")
    @ApiResponse(responseCode = "204", description = "Item returned")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}/{orderId}/refund")
    fun refund(@PathVariable userId: UUID, @PathVariable orderId: UUID) {
        storeService.refundPurchase(userId, orderId)
    }

    @Operation(summary = "Request warranty")
    @ApiResponse(responseCode = "200", description = "Warranty decision")
    @PostMapping("/{userId}/{orderId}/warranty", consumes = ["application/json"], produces = ["application/json"])
    fun warranty(@PathVariable userId: UUID,
                 @PathVariable orderId: UUID,
                 @Valid @RequestBody request: WarrantyRequest): WarrantyResponse {
        return storeService.warrantyRequest(userId, orderId, request)
    }
}