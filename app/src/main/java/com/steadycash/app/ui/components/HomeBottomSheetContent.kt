package com.steadycash.app.ui.components

/*import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.steadycash.app.ui.theme.DividerColor
import com.steadycash.app.ui.theme.IncomeGreen
import com.steadycash.app.ui.theme.PrimaryRed
import com.steadycash.app.ui.theme.TextPrimary
import com.steadycash.app.ui.theme.TextSecondary


@Composable
fun HomeBottomSheetContent(
    onAddIncome: () -> Unit = {},
    onAddExpense: () -> Unit = {},
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Text(
            text = "Quick add",
            color = TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))


        Button(
            onClick = {
                onAddIncome()
                onDismiss()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = IncomeGreen),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Add income", color = TextPrimary)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                onAddExpense()
                onDismiss()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Add expense", color = TextPrimary)
        }

        Spacer(modifier = Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(DividerColor)
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel", color = TextSecondary, fontSize = 16.sp)
        }
    }
}
*/
 
