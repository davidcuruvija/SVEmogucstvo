package com.davidcuruvija.svemogucstvo.screens.checkout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.davidcuruvija.svemogucstvo.util.formatPrice
import com.davidcuruvija.svemogucstvo.viewmodel.cart.CartViewModel

@Composable
fun CheckoutScreen(
    cartViewModel : CartViewModel,
    onReturnToCart : () -> Unit
) {
    val items by cartViewModel.items.collectAsStateWithLifecycle()
    val subtotal by cartViewModel.total.collectAsStateWithLifecycle()

    val shipping = 500L
    val total : Long = subtotal + shipping

    var email by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var apartment by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var sameBillingAddress by remember { mutableStateOf(true) }
    var addOrderNote by remember { mutableStateOf(false) }
    var orderNote by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "CHECKOUT DETAILS",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Contact information",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "We'll use this email to send you details and updates about your order.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = {
                Text("Email address")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Shipping address",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Enter the address where you want your order delivered.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = "Serbia",
            onValueChange = {},
            label = {
                Text("Country/Region")
            },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = {
                Text("First name")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = {
                Text("Last name")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = {
                Text("Address")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = apartment,
            onValueChange = { apartment = it },
            label = {
                Text("Apartment, suite, etc. (optional)")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = {
                Text("City")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = postalCode,
            onValueChange = { postalCode = it },
            label = {
                Text("Postal code")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = {
                Text("Phone (optional)")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = sameBillingAddress,
                onCheckedChange = {
                    sameBillingAddress = it
                }
            )

            Text("Use same address for billing")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Shipping options",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Flat rate",
                modifier = Modifier.weight(1f)
            )

            Text(
                text = formatPrice(shipping.toString())
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Payment options",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Cash on delivery",
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = "Pay with cash upon delivery.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = addOrderNote,
                onCheckedChange = {
                    addOrderNote = it
                }
            )

            Text("Add a note to your order")
        }

        if (addOrderNote) {
            OutlinedTextField(
                value = orderNote,
                onValueChange = { orderNote = it },
                label = {
                    Text("Order note")
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        HorizontalDivider()

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Order summary",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        items.forEach { item ->
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = item.productName,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = "Quantity: ${item.quantity}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    item.color?.let { color ->
                        Text(
                            text = "Color: $color",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    item.size?.let { size ->
                        Text(
                            text = "Size: $size",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Text(
                    text = formatPrice(
                        ((item.price.toLongOrNull() ?: 0L) * item.quantity)
                            .toString()
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        HorizontalDivider()

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Subtotal",
                modifier = Modifier.weight(1f)
            )

            Text(text = formatPrice(subtotal.toString()))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Flat rate",
                modifier = Modifier.weight(1f)
            )

            Text(text = formatPrice(shipping.toString()))
        }

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider()

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = formatPrice(total.toString()),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "By proceeding with your purchase you agree to our Terms and Conditions and Privacy Policy.",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                // TODO
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("PLACE ORDER")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = onReturnToCart,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("RETURN TO CART")
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}