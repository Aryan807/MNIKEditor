; Magic Functions

Function GetSpellColor(id, index)
	Local red = 0
	Local green = 0
	Local blue = 0

	Select id
	Case -1
		red=255
		blue=255
		green=255
	Case 0
		red=255
	Case 1
		red=255
		green=100
	Case 2
		red=255
		green=255
	Case 3
		green=255
	Case 4
		green=255
		blue=255
	Case 5
		blue=255
	Case 6
		red=255
		blue=255
	Case 7
		red=255
		blue=255
		green=255
	Case 8
		;red=128+120*Sin(LevelTimer Mod 360)
		;green=128+120*Cos(LevelTimer Mod 360)
		;blue=128-120*Sin(LevelTimer Mod 360)
		red = RainbowColorFunction(LevelTimer)
		green = RainbowColorFunction(LevelTimer+240)
		blue = RainbowColorFunction(LevelTimer+120)
	Case 9
		red=67
		blue=67
		green=67
	Case 10
		green=0
		red=107
		blue=153
	Case 11
		red=145
		green=109
		blue=35
	Case 12
		red=93
		green=2
		blue=7
		
	Case 13
		green=255
		blue=211
		red=138
	;Default
	;	red = 255
	;	green = 255
	;	blue = 255
	Case 14
		red=60
		blue=200
	Case 16
		Local purple = Abs(Sin(LevelTimer * 5) * 60)
		red = 180 + purple
		blue = 255
		green = 100
	End Select
	
	If index = 0 Then Return red
	If index = 1 Then Return green
	If index = 2 Then Return blue

	Return 255

End Function

Function GetMagicName$(id)
	If NegativeDestructive=True And id<0 Then Return "Negative Destructive";
	Select id
		Case 0
			Return "Floing"
		Case 1
			Return "Pow"
		Case 2
			Return "Pop"
		Case 3
			Return "Grow"
		Case 4
			Return "Brr"
		Case 5
			Return "Flash"
		Case 6
			Return "Blink"
		Case 7
			Return "Null"
		Case 8
			Return "Rainbow"
		Case 9
			Return "Blink Barrel"
		Case 10
			Return "Turret"
		Case 11
			Return "Barrel Reg"
		Case 12
			Return "Barrel TNT"
		Case 13
			Return "Nitrogen"
		Case 14
			Return "Flash Bubble"
		Case 16
			Return "Clone"
		
	End Select
End Function

Function GetMagicNameAndId$(id)
	Return Str(id) + ". " + GetMagicName(id)
End Function

;takes in a hue from 0° to 360° (well, 359 technically) and returns the red channel Value of that hue.
;you can offset the input by 120° and 240° to get the green and blue channels respectively
Function RainbowColorFunction#(h)
	h = h Mod 360
	If 0   <= h And h < 60  Then Return 255
	If 60  <= h And h < 120 Then Return 255*SmoothStep(120, 60, h)
	If 120 <= h And h < 240 Then Return 0
	If 240 <= h And h < 300 Then Return 255*SmoothStep(240,300, h)
	If 300 <= h And h < 360 Then Return 255
End Function