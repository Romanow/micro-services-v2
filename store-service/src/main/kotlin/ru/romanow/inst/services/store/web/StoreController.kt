package ru.romanow.inst.services.store.web

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import ru.romanow.inst.services.common.model.ErrorResponse
import ru.romanow.inst.services.store.model.*
import ru.romanow.inst.services.store.service.StoreService
import java.util.*
import javax.validation.Valid

@Tag(name = "Store API")
@RestController
@RequestMapping("/api/v1/store")
class StoreController(
    private val storeService: StoreService,
) {
    @Operation(summary = "List user orders")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User orders info"),
            ApiResponse(
                responseCode = "404",
                description = "User not found",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "422",
                description = "External request failed",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @GetMapping("/orders", produces = ["application/json"])
    fun orders(authenticationToken: JwtAuthenticationToken): UserOrdersResponse {
        val userId = extractUserId(authenticationToken.token)
        return storeService.findUserOrders(userId)
    }

    @Operation(summary = "User order info")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User order info"),
            ApiResponse(
                responseCode = "404",
                description = "User not found",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "422",
                description = "External request failed",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @GetMapping("/{orderUid}", produces = ["application/json"])
    fun orders(authenticationToken: JwtAuthenticationToken, @PathVariable orderUid: UUID): UserOrderResponse {
        val userId = extractUserId(authenticationToken.token)
        return storeService.findUserOrder(userId, orderUid)
    }

    @Operation(summary = "Purchase item")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Item purchased"),
            ApiResponse(
                responseCode = "400",
                description = "Bad request format",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "User not found",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "409",
                description = "Item not available",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "422",
                description = "External request failed",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PostMapping("/purchase", consumes = ["application/json"])
    fun purchase(
        @Valid @RequestBody request: PurchaseRequest,
        authenticationToken: JwtAuthenticationToken,
    ): ResponseEntity<Void> {
        val userId = extractUserId(authenticationToken.token)
        val orderUid: UUID = storeService.makePurchase(userId, request)
        val uri = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{orderUid}")
            .buildAndExpand(orderUid)
            .toUri()

        return ResponseEntity.created(uri).build()
    }

    @Operation(summary = "Return items")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Item returned"),
            ApiResponse(
                responseCode = "404",
                description = "User not found",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "422",
                description = "External request failed",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{orderUid}/refund")
    fun refund(@PathVariable orderUid: UUID, authenticationToken: JwtAuthenticationToken) {
        val userId = extractUserId(authenticationToken.token)
        storeService.refundPurchase(userId, orderUid)
    }

    @Operation(summary = "Request warranty")
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
                description = "User not found",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "422",
                description = "External request failed",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PostMapping("/{orderUid}/warranty", consumes = ["application/json"], produces = ["application/json"])
    fun warranty(
        @PathVariable orderUid: UUID,
        @Valid @RequestBody request: WarrantyRequest,
        authenticationToken: JwtAuthenticationToken,
    ): WarrantyResponse {
        val userId = extractUserId(authenticationToken.token)
        return storeService.warrantyRequest(userId, orderUid, request)
    }

    private fun extractUserId(token: Jwt) = (token.claims["sub"] as String).substringAfter("|")
}
