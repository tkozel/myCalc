package cz.uhk.mycalc

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.uhk.mycalc.ui.theme.MyCalcTheme

class MainActivity : ComponentActivity() {
    private lateinit var oper: String
    private var first: Double = 0.0
    private var display : String = "0"
    private var clear = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyCalcTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var dispValue by rememberSaveable {
                        mutableStateOf(display)
                    }
                    Calculator(dispValue) {
                        clickHandler(it)
                        dispValue = display //zmena stavu displeje
                    }
                }
            }
        }
    }

    /**
     * Obsluha kliknuti na tlacitko
     */
    private fun clickHandler(it: String) {
        when  {
            it == "C" -> display = "0"
            it[0].isDigit() -> {
                display = if (clear || display=="0") it else display + it
                clear = false
            }
            "+-*/".contains(it) -> {
                first = display.toDouble()
                clear = true
                oper = it
            }
            it == "=" -> calculate()
        }

        Log.d("DISP", display)
    }

    /**
     * Dopocita na display vysledek pro zadanou operaci
     */
    private fun calculate() {
        val second = display.toDouble()
        val res = when (oper) {
            "+" -> first + second
            "-" -> first - second
            "*" -> first * second
            "/" -> first / second
            else -> 0.0
        }
        display = res.toString()
    }


}

/**
 * UI kalkulacky
 */
@Composable
fun Calculator(dispValue: String, onClick : (String)->Unit) {
    Column (
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Display(dispValue)
        Keyboard(onClick)
    }
}

val labels = listOf(
    "7","8","9","/",
    "4","5","6","*",
    "1","2","3","+",
    "C","0","=","-"
    )

/**
 * Klavesnice kalkulacky
 */
@Composable
fun Keyboard(onClick: (String) -> Unit) {
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxSize()
    ) {
        labels.windowed(4, 4).forEach {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                it.forEach {
                    Button(
                        onClick = { onClick(it) },
                        modifier = Modifier
                            .size(100.dp)
                            .padding(5.dp)
                    ) {
                        Text(text = it, fontSize = 36.sp)
                    }
                }
            }

        }
    }
}

/**
 * Displej kalkulacky
 */
@Composable
fun Display(dispValue: String) {
    Card (
        border = BorderStroke(1.dp, Color.Black),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .padding(8.dp)
            .shadow(elevation = 8.dp)
            .fillMaxWidth()
    ){
        Text(
            text = dispValue,
            fontFamily = FontFamily.Monospace,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xff10f090))
                .padding(8.dp)
        )
    }

}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    MyCalcTheme {
        Calculator("0") {}
    }
}