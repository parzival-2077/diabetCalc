package com.example.diabetcalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diabetcalc.ui.theme.DiabetCalcTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiabetCalcTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalcScreen()
                }
            }
        }
    }
}
@Composable
fun CalcScreen(){
    var uglevod by remember {mutableStateOf("")}
    val uglevodInt =uglevod.toDoubleOrNull()?:0.0
    var ves by remember{ mutableStateOf("")}
    val vesInt =ves.toDoubleOrNull()?:0.0
    var insulinUp by remember { mutableStateOf(false)}
    val uglevodItog=calcItogUglevod(uglevodInt,vesInt)
    val hbItog=calcItogHb(uglevodItog)
    val insulinItog=calcItogIn(hbItog,insulinUp)
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter= painterResource(R.drawable.high_blood_sugar11),
            contentDescription = "stringResource(id = R.string.dog_content_description)",
            modifier=Modifier
                .size(75.dp)
                .clip(CircleShape)
                .border(
                    BorderStroke(4.dp, Color.White),
                    CircleShape
                )
        )
        Text(
            text = stringResource(R.string.firstTitle),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)

        )

        EditNumberFailed(
            value =uglevod ,

            onValueChange ={uglevod=it} ,
            lable =R.string.uglevod1,
            leadingIcon = R.drawable.scale_fill0_wght400_grad0_opsz24,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )

        EditNumberFailed(
            value =ves ,
            leadingIcon = R.drawable.lunch_dining_fill0_wght400_grad0_opsz24,
            onValueChange ={ves=it} ,
            lable =R.string.ves,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
        )
        //Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = stringResource(R.string.itog, uglevodItog),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier
                .padding(bottom = 10.dp, top = 1.dp)
                .align(alignment = Alignment.Start)
        )
        //Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = stringResource(R.string.itogHb, hbItog),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier
                .padding(top = 1.dp)
                .align(alignment = Alignment.Start)
        )
        Row(modifier= Modifier
            .fillMaxWidth()
            .size(48.dp),
            verticalAlignment = Alignment.CenterVertically){
            Text(
                text = stringResource(R.string.iinsulin,insulinItog),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,


                )
            insulinRow(
                insulinUp = insulinUp,
                onInsulinChange ={insulinUp=it},
            )

        }

    }
}
@Composable
fun EditNumberFailed(
    value:String,
    onValueChange:(String)->Unit,
    @StringRes lable:Int,
    @DrawableRes leadingIcon:Int,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier=Modifier
    ) {


    TextField(
        value = value,
        onValueChange = onValueChange,
        leadingIcon={ Icon(painterResource(id = leadingIcon),null) },
        singleLine = true,
        label = { Text(stringResource(lable)) },
        modifier=modifier,
        keyboardOptions = keyboardOptions
    )

}
@Composable
fun insulinRow(insulinUp:Boolean,
               onInsulinChange:(Boolean) ->Unit,
               modifier: Modifier=Modifier){

    Row(modifier= modifier
        .fillMaxWidth()
        .size(48.dp),
        verticalAlignment = Alignment.CenterVertically)
    {

        Switch(
            checked =insulinUp ,
            onCheckedChange =onInsulinChange,
            modifier= modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
        )
    }
}

internal fun calcItogUglevod(uglevodInt:Double,vesInt:Double): Double {
    var uglevodItog = uglevodInt * vesInt /100
    uglevodItog=(uglevodItog* 100).roundToInt() / 100.0
    return uglevodItog

}
internal fun calcItogHb(uglevodItog:Double):Double{
    var hbItog=uglevodItog/12
    hbItog=(hbItog* 100).roundToInt() / 100.0
    return hbItog
}
internal  fun calcItogIn(hbItog:Double,insulinUp: Boolean):Double{
    var insulinItog:Double
    if(insulinUp==true){
         insulinItog=hbItog*2
    }
    else {
        insulinItog=hbItog*1.5
    }

    return (insulinItog* 100).roundToInt() / 100.0
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DiabetCalcTheme {
        CalcScreen()
    }
}