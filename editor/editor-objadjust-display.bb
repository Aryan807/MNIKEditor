Function DisplayObjectAdjuster(i)

	tex2$=ObjectAdjuster$(i)

	CurrentAdjusterRandomized=False
	CurrentAdjusterAbsolute=True
	CurrentAdjusterZero=False
	LeftAdj$=""
	RightAdj$=""

	StartX=SidebarX+10
	StartY=SidebarY+305
	StartY=StartY+15+(i-ObjectAdjusterStart)*15

	Select ObjectAdjuster$(i)
	Case "TextureName"
		tex2$="Texture"
		tex$=CurrentObject\Attributes\TexName$
		tex$=SetAdjusterDisplayString(ObjectAdjusterTextureName,CurrentObject\Attributes\TexName$,tex$)

	Case "ModelName"
		tex2$="Model"
		tex$=CurrentObject\Attributes\ModelName$
		tex$=SetAdjusterDisplayString(ObjectAdjusterModelName,CurrentObject\Attributes\ModelName$,tex$)

	Case "X"
		tex$=Str$(CurrentObject\Position\X)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterX,CurrentObject\Position\X,tex$)
	Case "Y"
		tex$=Str$(CurrentObject\Position\Y)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterY,CurrentObject\Position\Y,tex$)
	Case "Z"
		tex$=Str$(CurrentObject\Position\Z)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterZ,CurrentObject\Position\Z,tex$)

	Case "XAdjust"
		tex$=Str$(CurrentObject\Attributes\XAdjust)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterXAdjust,CurrentObject\Attributes\XAdjust,tex$)
	Case "YAdjust"
		tex$=Str$(CurrentObject\Attributes\YAdjust)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterYAdjust,CurrentObject\Attributes\YAdjust,tex$)
	Case "ZAdjust"
		tex$=Str$(CurrentObject\Attributes\ZAdjust)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterZAdjust,CurrentObject\Attributes\ZAdjust,tex$)

	Case "XScale"
		tex$=Str$(CurrentObject\Attributes\XScale)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterXScale,CurrentObject\Attributes\XScale,tex$)
	Case "YScale"
		tex$=Str$(CurrentObject\Attributes\YScale)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterYScale,CurrentObject\Attributes\YScale,tex$)
	Case "ZScale"
		tex$=Str$(CurrentObject\Attributes\ZScale)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterZScale,CurrentObject\Attributes\ZScale,tex$)

	Case "DefensePower"
		tex$=Str$(CurrentObject\Attributes\DefensePower)
		tex2$="Greeting"
		Select CurrentObject\Attributes\DefensePower
		Case 0
			tex$="Stinky1"
		Case 1
			tex$="Stinky2"
		Case 2
			tex$="Loof1"
		Case 3
			tex$="Loof2"
		Case 4
			tex$="Qookie1"
		Case 5
			tex$="Qookie2"
		Case 6
			tex$="Peegue1"
		Case 7
			tex$="Peegue2"
		Case 8
			tex$="Qookie3"
		Case 9
			tex$="Qookie4"

		Case 10,11,12,13,14,15,16,17,18
			tex$="Wee "+Str$(CurrentObject\Attributes\DefensePower-9)
		Case 19,20,21
			tex$="Kaboom "+Str$(CurrentObject\Attributes\DefensePower-18)

		Case 22,23,24
			tex$="ZBot "+Str$(CurrentObject\Attributes\DefensePower-21)

		Case 25
			tex$="Chomper"
		Case 26
			tex$="Thwart 1"
		Case 27
			tex$="Thwart 2"
		Case 28
			tex$="Troll 1"
		Case 29
			tex$="Troll 2"
		Case 30
			tex$="Monster"
		Case 31
			tex$="Stinky 3"
		Case 32
			tex$="Stinky 4"
		Case 33
			tex$="Stinky 5"
		Default
			tex$="Victory"

		End Select

		tex$=SetAdjusterDisplayInt(ObjectAdjusterDefensePower,CurrentObject\Attributes\DefensePower,tex$)

	Case "AttackPower"
		tex$=Str$(CurrentObject\Attributes\AttackPower)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterAttackPower,CurrentObject\Attributes\AttackPower,tex$)

	Case "DestructionType"
		tex$=Str$(CurrentObject\Attributes\DestructionType)

		Select CurrentObject\Attributes\DestructionType
			Case 0
				tex$="None"
			Case 1
				tex$="White"
			Case 2
				tex$="MODDED" ; Purple
		End Select

		tex$=SetAdjusterDisplayInt(ObjectAdjusterDestructionType,CurrentObject\Attributes\DestructionType,tex$)

	Case "YawAdjust"
		tex$=Str$(CurrentObject\Attributes\YawAdjust)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterYawAdjust,CurrentObject\Attributes\YawAdjust,tex$)
	Case "PitchAdjust"
		tex$=Str$(CurrentObject\Attributes\PitchAdjust)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterPitchAdjust,CurrentObject\Attributes\PitchAdjust,tex$)
	Case "RollAdjust"
		tex$=Str$(CurrentObject\Attributes\RollAdjust)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterRollAdjust,CurrentObject\Attributes\RollAdjust,tex$)

	Case "ID"
		tex$=Str$(CurrentObject\Attributes\ID)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterID,CurrentObject\Attributes\ID,tex$)
	Case "Type"
		tex$=Str$(CurrentObject\Attributes\LogicType)+"/"+GetTypeString$(CurrentObject\Attributes\LogicType)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterLogicType,CurrentObject\Attributes\LogicType,tex$)
	Case "SubType"
		tex$=Str$(CurrentObject\Attributes\LogicSubType)

		If CurrentObject\Attributes\ModelName$="!Crab"
			tex2$="Color"
			If CurrentObject\Attributes\LogicSubType=0
				tex$="Green"
			Else If CurrentObject\Attributes\LogicSubType=1
				tex$="Red"
			EndIf
		EndIf

		If CurrentObject\Attributes\LogicType=165 ; Arcade Cabinet
			If CurrentObject\Attributes\LogicSubType=1
				tex$="Sold Out"
			Else
				tex$="Available"
			EndIf
			tex$=CurrentObject\Attributes\LogicSubType+"/"+tex$
		EndIf

		If CurrentObject\Attributes\LogicType=179 ; Custom Item
			tex2$="Fn"
			If CurrentObject\Attributes\LogicSubType>=0 And CurrentObject\Attributes\LogicSubType<30
				tex$=GetItemFnName$(4001+CurrentObject\Attributes\LogicSubType*10)
			Else If CurrentObject\Attributes\LogicSubType=-1
				tex$="Gloves"
			Else If CurrentObject\Attributes\LogicSubType=-2
				tex$="Lamp"
			Else If CurrentObject\Attributes\LogicSubType=-3
				tex$="GlowGem"
			Else If CurrentObject\Attributes\LogicSubType=-4
				tex$="Spy-Eye"
			Else If CurrentObject\Attributes\LogicSubType=-5
				tex$="Glyph"
			Else If CurrentObject\Attributes\LogicSubType=-6
				tex$="MapPiece"
			Else
				tex$="Raw"+GetItemFnName$(4001+CurrentObject\Attributes\LogicSubType*10)
			EndIf
		EndIf
		If CurrentObject\Attributes\LogicType=190 ; Particle Emitter
			Select CurrentObject\Attributes\LogicSubType
			Case 1
				tex$="Steam"
			Case 2
				tex$="Splish"
			Case 3
				tex$="Spray"
			Case 4
				tex$="Sparks"
			Case 5
				tex$="Blinker"
			Case 6
				tex$="CircleBurst"
			Case 7
				tex$="Spiral"
			End Select
		EndIf
		If CurrentObject\Attributes\LogicType=200 ; Magic charger
			If CurrentObject\Attributes\LogicSubType=0
				tex$=CurrentObject\Attributes\LogicSubType+"/Regular"
			ElseIf CurrentObject\Attributes\LogicSubType=1
				tex$=CurrentObject\Attributes\LogicSubType+"/Faint"
			Else
				tex$=CurrentObject\Attributes\LogicSubType+"/OneByOne"
			EndIf
		EndIf
		If CurrentObject\Attributes\LogicType=230 ; FireFlower
			tex2$="Turning"
			If CurrentObject\Attributes\LogicSubType=0
				tex$="None"
			Else If CurrentObject\Attributes\LogicSubType=1
				tex$="Player"
			Else If CurrentObject\Attributes\LogicSubType=2
				tex$="ClockW"
			Else If CurrentObject\Attributes\LogicSubType=3
				tex$="CountW"
			EndIf
		EndIf
		If CurrentObject\Attributes\LogicType=370 ; Crab
			tex2$="Color"
			If CurrentObject\Attributes\LogicSubType=0
				tex$="Green"
			Else If CurrentObject\Attributes\LogicSubType=1
				tex$="Red"
			EndIf
		EndIf
		If CurrentObject\Attributes\LogicType=50 ; spellball
			tex2$="Spell"
			tex$=GetMagicNameAndId$(CurrentObject\Attributes\LogicSubType)
		EndIf
		If CurrentObject\Attributes\LogicType=54 ; Magic Mirror
			tex2$="Glyph"
			Select CurrentObject\Attributes\LogicSubType
			Case 0
				tex$="Inactive"
			Case 1
				tex$="Fire"
			Case 2
				tex$="Ice"
			Case 3
				tex$="Time"
			Case 4
				tex$="Acid"
			Case 5
				tex$="Home"
			End Select
		EndIf
		If CurrentObject\Attributes\LogicType=90 ; button
			Select CurrentObject\Attributes\LogicSubType
			Case 0
				tex$="Square"
			Case 1
				tex$="Round"
			Case 2
				tex$="DiamondOnce"
			Case 3
				tex$="Diamond"
			Case 4
				tex$="Star"
			Case 5
				tex$="X2Y Square"
			Case 6
				tex$="X2Y Round"
			Case 7
				tex$="X2Y Once"
			Case 8
				tex$="X2Y Diamond"
			Case 9
				tex$="X2Y Star"
			Case 10
				tex$="LevelExit"
			Case 11
				tex$="NPC Modifier"
			Case 12
				tex$="FakeStnkerExit"
			Case 13
				tex$="AdventureStar"
			Case 14
				tex$="AdventuredStar"
			Case 15
				tex$="GeneralCommand"
			Case 16
				tex$="Rotator"
			Case 32
				tex$="InvSquare"
			Case 33
				tex$="InvRound"
			Case 34
				tex$="InvOnce"
			Case 35
				tex$="InvDiamond"
			Case 36
				tex$="InvStar"
			Case 37
				tex$="InvX2Y Square"
			Case 38
				tex$="InvX2Y Round"
			Case 39
				tex$="InvX2Y Once"
			Case 40
				tex$="InvX2Y Diamond"
			Case 41
				tex$="InvX2Y Star"
			Case 48
				tex$="InvRotator"
			End Select
		EndIf

		If CurrentObject\Attributes\LogicType=120 Or CurrentObject\Attributes\LogicType=400 ; Wee Stinker or Baby Boomer
			Select CurrentObject\Attributes\LogicSubType
			Case -2
				tex$="Dying?"
			Case -1
				tex$="Exiting"
			Case 0
				tex$="Asleep"
			Case 1
				tex$="Following"
			Case 2
				tex$="Directed"
			Case 3
				tex$="FallingAsleep"
			Case 4
				tex$="CagedDirected"
			Case 5
				tex$="CagedFollow"
			End Select
		EndIf

		If CurrentObject\Attributes\LogicType=434 ; Mothership
			tex2$="AudioTimeOffset"
		EndIf

		tex$=SetAdjusterDisplayInt(ObjectAdjusterLogicSubType,CurrentObject\Attributes\LogicSubType,tex$)

	Case "TimerMax1"
		tex$=Str$(CurrentObject\Attributes\TimerMax1)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterTimerMax1,CurrentObject\Attributes\TimerMax1,tex$)

		If CurrentObject\Attributes\LogicType=20 ; Fire trap
			tex2$=" TimerOn" ; Extra space to align it with TimerOff.
		ElseIf CurrentObject\Attributes\LogicType=40 ; Bridge
			tex2$="TimerToggle"
		ElseIf CurrentObject\Attributes\LogicType=230 ; FireFlower
			tex2$="TimerShoot"
		EndIf
	Case "TimerMax2"
		tex$=Str$(CurrentObject\Attributes\TimerMax2)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterTimerMax2,CurrentObject\Attributes\TimerMax2,tex$)

		If CurrentObject\Attributes\LogicType=20 ; Fire trap
			tex2$="TimerOff"
		EndIf
	Case "Timer"
		tex$=Str$(CurrentObject\Attributes\Timer)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterTimer,CurrentObject\Attributes\Timer,tex$)

		If CurrentObject\Attributes\LogicType=20 ; Fire trap
			tex2$="StartDelay"
		ElseIf CurrentObject\Attributes\LogicType=40 ; Bridge
			tex2$="StartDelay"
		ElseIf CurrentObject\Attributes\LogicType=230 Or CurrentObject\Attributes\LogicType=290 ; FireFlower or Thwart
			tex2$="AttackDelay"
		EndIf

	Case "TextData0"
		; custom model
		tex2$=""
		tex$=CurrentObject\Attributes\TextData0$
		tex$=SetAdjusterDisplayString(ObjectAdjusterTextData0,CurrentObject\Attributes\TextData0$,tex$)

	Case "TextData1"
		tex2$=""
		tex$=CurrentObject\Attributes\TextData1$
		tex$=SetAdjusterDisplayString(ObjectAdjusterTextData1,CurrentObject\Attributes\TextData1$,tex$)

	Case "Active"
		If CurrentObject\Attributes\Active=0
			tex$="No (0)"
		Else If CurrentObject\Attributes\Active=1001
			tex$="Yes (1001)"
		Else If CurrentObject\Attributes\Active Mod 2=0
			tex$="Soon No ("+CurrentObject\Attributes\Active+")"
		Else
			tex$="Soon Yes ("+CurrentObject\Attributes\Active+")"
		EndIf
		tex$=SetAdjusterDisplayInt(ObjectAdjusterActive,CurrentObject\Attributes\Active,tex$)

	Case "ActivationSpeed"
		tex$=Str$(CurrentObject\Attributes\ActivationSpeed)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterActivationSpeed,CurrentObject\Attributes\ActivationSpeed,tex$)

	Case "ActivationType"
		tex$=GetActivationTypeString$(CurrentObject\Attributes\ActivationType)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterActivationType,CurrentObject\Attributes\ActivationType,tex$)

	Case "ButtonPush"
		tex$=CurrentObject\Attributes\ButtonPush+"/"+OneToYes$(CurrentObject\Attributes\ButtonPush)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterButtonPush,CurrentObject\Attributes\ButtonPush,tex$)

	Case "WaterReact"
		tex$=Str$(CurrentObject\Attributes\WaterReact)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterWaterReact,CurrentObject\Attributes\WaterReact,tex$)

	Case "Freezable"
		tex$=Str$(CurrentObject\Attributes\Freezable)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterFreezable,CurrentObject\Attributes\Freezable,tex$)

	Case "Frozen"
		tex$=Str$(CurrentObject\Attributes\Frozen)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterFrozen,CurrentObject\Attributes\Frozen,tex$)

	Case "Teleportable"
		tex$=CurrentObject\Attributes\Teleportable+"/"+OneToYes$(CurrentObject\Attributes\Teleportable)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterTeleportable,CurrentObject\Attributes\Teleportable,tex$)

	Case "Data0"
		tex$=Str$(CurrentObject\Attributes\Data0)

		If CurrentObject\Attributes\LogicType=160 And CurrentObject\Attributes\ModelName$="!CustomModel"
			tex2$="YawAnim"
		EndIf

		If CurrentObject\Attributes\LogicType=160 And CurrentObject\Attributes\ModelName$="!Obstacle48" ; (wysp ship)
			tex2$="Turning"
			Select CurrentObject\Attributes\Data0
				Case 0
					tex$="Yes"
				Default
					tex$="No"
			End Select
			tex$=CurrentObject\Attributes\Data0+"/"+tex$
		EndIf

		If CurrentObject\Attributes\ModelName$="!Scritter" Or CurrentObject\Attributes\ModelName$="!Cuboid" Or CurrentObject\Attributes\ModelName$="!Spring" Or CurrentObject\Attributes\ModelName$="!SteppingStone" Or CurrentObject\Attributes\ModelName$="!Transporter" Or CurrentObject\Attributes\ModelName$="!ColourGate" Or CurrentObject\Attributes\ModelName$="!Key" Or CurrentObject\Attributes\ModelName$="!KeyCard" Or CurrentObject\Attributes\ModelName$="!Teleport" Or CurrentObject\Attributes\ModelName$="!FlipBridge" Or CurrentObject\Attributes\ModelName$="!Pushbot" Or CurrentObject\Attributes\ModelName$="!Suctube" Or CurrentObject\Attributes\ModelName$="!Conveyor"
			tex2$="Colour"
		EndIf

		If CurrentObject\Attributes\ModelName$="!Obstacle51" Or CurrentObject\Attributes\ModelName$="!Obstacle55" Or CurrentObject\Attributes\ModelName$="!Obstacle59"
			tex2$="Shape"
		EndIf

		If CurrentObject\Attributes\ModelName$="!CustomItem"
			tex2$="Texture"
		EndIf

		If CurrentObject\Attributes\ModelName$="!WaterFall"
			tex2$="Type"
			Select CurrentObject\Attributes\Data0
				Case 0
					tex$="Water"
				Case 1
					tex$="Lava"
				Case 2
					tex$="Green"
			End Select
		EndIf

		If CurrentObject\Attributes\ModelName$="!Gem"
			tex2$="Shape"
		EndIf
		If CurrentObject\Attributes\ModelName$="!Sign"
			tex2$="Shape"
		EndIf
		If CurrentObject\Attributes\ModelName$="!Crystal"
			tex2$="Type"
			Select CurrentObject\Attributes\Data0
			Case 0
				tex$="Rainbow"
			Case 1
				tex$="Void"
			End Select

		EndIf

		If CurrentObject\Attributes\ModelName$="!Kaboom"
			tex2$="Texture"

			Select CurrentObject\Attributes\Data0
			Case 1
				tex$="Blue"
			Case 2
				tex$="Purple"
			Case 3
				tex$="Red"
			Case 4
				tex$="Gold"
			Case 5
				tex$="Dark"
			End Select

		EndIf

		If CurrentObject\Attributes\ModelName$="!Wisp"
			tex2$="Texture"
		EndIf

		If CurrentObject\Attributes\ModelName$="!Retrozbot"
			tex2$="Direction"

			Select CurrentObject\Attributes\Data0
			Case 0
				tex$="North"
			Case 1
				tex$="East"
			Case 2
				tex$="South"
			Case 3
				tex$="West"
			End Select
		EndIf

		If CurrentObject\Attributes\ModelName$="!Sun Sphere1"
			tex2$="Red"
		EndIf

		If CurrentObject\Attributes\ModelName$="!GrowFlower"
			tex2$="TileLogic"
			tex$=LogicIdToLogicName$(CurrentObject\Attributes\Data0)
		EndIf

		; Q - player NPC functionality
		If IsModelNPC(CurrentObject\Attributes\ModelName$) Or CurrentObject\Attributes\LogicType=110
			tex2$="Texture"

			Select CurrentObject\Attributes\Data0
			Case 1
				tex$="1/Blue"
			Case 2
				tex$="2/Purple"
			Case 3
				tex$="3/Red"
			Case 4
				tex$="4/Dark"
			Case 5
				tex$="5/Shadow"
			Case 6
				tex$="6/Fire"
			Case 7
				tex$="7/Green"
			Case 8
				tex$="8/White"
			End Select
		EndIf

		; Model checks are separated from Type checks so that the Type can override the model.

		If CurrentObject\Attributes\LogicType=51 Or CurrentObject\Attributes\LogicType=200 Or CurrentObject\Attributes\LogicType=201 ; spellball generator or glovecharge or glove discharge
			tex2$="Spell"
			tex$=GetMagicNameAndId(CurrentObject\Attributes\Data0)
		EndIf

		If CurrentObject\Attributes\LogicType=179 ; Custom Item
			tex2$="Texture"
		EndIf

		If CurrentObject\Attributes\LogicType=320 ; Void
			tex2$="TimeOffset"
			If CurrentObject\Attributes\Data0=0
				tex$="Random"
			EndIf
		EndIf

		If CurrentObject\Attributes\LogicType=350 ; GrowFlower
			tex2$="TileLogic"
			tex$=LogicIdToLogicName$(CurrentObject\Attributes\Data0)
		EndIf

		If CurrentObject\Attributes\LogicType=280 Or CurrentObject\Attributes\LogicType=40 Or CurrentObject\Attributes\LogicType=210 Or CurrentObject\Attributes\LogicType=10 Or CurrentObject\Attributes\LogicType=172 Or CurrentObject\Attributes\LogicType=30 Or CurrentObject\Attributes\LogicType=140 Or CurrentObject\Attributes\LogicType=20 Or CurrentObject\Attributes\LogicType=410 Or CurrentObject\Attributes\LogicType=424 Or CurrentObject\Attributes\LogicType=432 Or CurrentObject\Attributes\LogicType=281 Or CurrentObject\Attributes\LogicType=45 Or CurrentObject\Attributes\LogicType=46
			tex2$="Colour"
		EndIf

		If IsObjectTypeKeyblock(CurrentObject\Attributes\LogicType)
			tex2$="CMD"
			tex$=Str(CurrentObject\Attributes\Data0)+"/"+GetCommandName$(CurrentObject\Attributes\Data0)
		EndIf

		If CurrentObject\Attributes\LogicType=90 ; button
			If IsObjectSubTypeFourColorButton(CurrentObject\Attributes\LogicSubType)
				tex2$="Colour1"
			Else If (CurrentObject\Attributes\LogicSubType Mod 32)<10 ; ColX2Y Button
				tex2$="Col From"
			Else If (CurrentObject\Attributes\LogicSubType Mod 32)=15 ; General Command
				tex2$="CMD"
				tex$=Str(CurrentObject\Attributes\Data0)+"/"+GetCommandName$(CurrentObject\Attributes\Data0)
			Else If (CurrentObject\Attributes\LogicSubType Mod 32)=16 Or (CurrentObject\Attributes\LogicSubType Mod 32)=17 ; Rotator or ???
				tex2$="Colour"
			Else If CurrentObject\Attributes\LogicSubType=13 ; Adventure Star
				tex2$="Adventure ID"
			EndIf
		EndIf

		If CurrentObject\Attributes\LogicType=50 ; spellball
			tex2$="GoalX"
		EndIf
		If CurrentObject\Attributes\LogicType=190 Or CurrentObject\Attributes\LogicType=164 ; Particle Spawner or Fountain
			tex2$="Particle ID"
		EndIf
		If CurrentObject\Attributes\LogicType=11 ; TollGate
			tex2$="Cost"
		EndIf

		If CurrentObject\Attributes\LogicType=165 ; Arcade Cabinet
			tex2$="Activates"
			Data0=CurrentObject\Attributes\Data0
			tex$=Data0
			While True
				Data0=Data0+1
				If ((Data0-200) Mod 3)=0
					Exit
				Else
					tex$=tex$+", "+Data0
				EndIf
			Wend
		EndIf

		If CurrentObject\Attributes\LogicType=40 ; bridge
			tex$=Str$(CurrentObject\Attributes\Data0+8)
		EndIf

		If CurrentObject\Attributes\LogicType=70 ; Beta Pickup Item
			tex2$="Fn"
			tex$=GetItemFnName$(CurrentObject\Attributes\Data0)
		EndIf

		If CurrentObject\Attributes\LogicType=260 ; spikeyball
			tex2$="Direction"
			If CurrentObject\Attributes\Data1=2
				Select CurrentObject\Attributes\Data0
				Case 0
					tex$="North"
				Case 1
					tex$="NorthEast"
				Case 2
					tex$="East"
				Case 3
					tex$="SouthEast"
				Case 4
					tex$="South"
				Case 5
					tex$="SouthWest"
				Case 6
					tex$="West"
				Case 7
					tex$="NorthWest"
				End Select

			Else
				Select CurrentObject\Attributes\Data0
				Case 0
					tex$="North"
				Case 1
					tex$="East"
				Case 2
					tex$="South"
				Case 3
					tex$="West"
				End Select

			EndIf
		EndIf
		If CurrentObject\Attributes\LogicType=250 ; chomper
			tex2$="Speed"
			tex$="+"+Str$(CurrentObject\Attributes\Data0)
		EndIf
		If CurrentObject\Attributes\LogicType=230 ; fireflower
			tex2$="Direction"

			Select CurrentObject\Attributes\Data0
			Case 0
				tex$="North"
			Case 1
				tex$="NorthEast"
			Case 2
				tex$="East"
			Case 3
				tex$="SouthEast"
			Case 4
				tex$="South"
			Case 5
				tex$="SouthWest"
			Case 6
				tex$="West"
			Case 7
				tex$="NorthWest"
			End Select
		EndIf
		; turtle or scouge or ufo or retro z-bot or zipbot or zapbot
		If CurrentObject\Attributes\LogicType=220 Or CurrentObject\Attributes\LogicType=421 Or CurrentObject\Attributes\LogicType=422 Or CurrentObject\Attributes\LogicType=423 Or CurrentObject\Attributes\LogicType=430 Or CurrentObject\Attributes\LogicType=431
			tex2$="Direction"

			Select CurrentObject\Attributes\Data0
			Case 0
				tex$="North"
			Case 1
				tex$="East"
			Case 2
				tex$="South"
			Case 3
				tex$="West"
			End Select
		EndIf

		If CurrentObject\Attributes\LogicType=300 Or CurrentObject\Attributes\LogicType=301 ; Brr float or rainbow float
			tex2$="Deactivating"
			If CurrentObject\Attributes\Data0=0
				tex$="NotYet"
			ElseIf CurrentObject\Attributes\Data0>0
				tex$=tex$+"/Soon"
			EndIf
		EndIf

		If CurrentObject\Attributes\LogicType=310 ; duck
			tex2$="Move"
			If CurrentObject\Attributes\Data0=1
				tex$="Yes"
			Else
				tex$="No"
			EndIf
			tex$=CurrentObject\Attributes\Data0+"/"+tex$
		EndIf

		If CurrentObject\Attributes\LogicType=433 ; Z-Bot NPC
			tex2$="Exploding"
			If CurrentObject\Attributes\Data0<=0
				tex$="No"
			ElseIf CurrentObject\Attributes\Data0<120
				tex$="Yes"
			Else
				tex$="Always"
			EndIf
			tex$=CurrentObject\Attributes\Data0+"/"+tex$
		EndIf

		If CurrentObject\Attributes\LogicType=434 ; mothership
			tex2$="SpawnTimer" ; Formerly TimerMax
			If CurrentObject\Attributes\Data0=0
				tex$="No Spawns"
			EndIf
		EndIf

		If CurrentObject\Attributes\LogicType=460 ; BurstFlower
			tex2$="TimeOffset"
		EndIf

		If CurrentObject\Attributes\LogicType=470 Or CurrentObject\Attributes\LogicType=471 ; ghost or wraith
			tex2$="Radius"
		EndIf

		If CurrentObject\Attributes\LogicType=52 ; meteor shooter
			tex2$="StartZ"
		EndIf

		tex$=SetAdjusterDisplayInt(ObjectAdjusterData0,CurrentObject\Attributes\Data0,tex$)

	Case "Data1"
		tex$=Str$(CurrentObject\Attributes\Data1)

		If CurrentObject\Attributes\ModelName$="!Obstacle51" Or CurrentObject\Attributes\ModelName$="!Obstacle55" Or CurrentObject\Attributes\ModelName$="!Obstacle59"
			tex2$="Texture"
		EndIf

		If CurrentObject\Attributes\ModelName$="!Gem"
			tex2$="Colour"
		EndIf

		If CurrentObject\Attributes\ModelName$="!FlipBridge"
			tex2$="SubColour"
		EndIf

		If CurrentObject\Attributes\LogicType=160 And CurrentObject\Attributes\ModelName$="!CustomModel"
			tex2$="PitchAnim"
		EndIf

		If CurrentObject\Attributes\ModelName$="!Chomper"
			tex2$="Special"
			If CurrentObject\Attributes\Data1=0
				tex$="---"
			Else If CurrentObject\Attributes\Data1=1
				tex$="Ghost"
			Else If CurrentObject\Attributes\Data1=2
				tex$="Glow"
			Else If CurrentObject\Attributes\Data1=3
				tex$="Mecha"
			EndIf
		EndIf

		If CurrentObject\Attributes\ModelName$="!Sun Sphere1"
			tex2$="Green"
		EndIf

		If CurrentObject\Attributes\ModelName$="!Sign"
			tex2$="Texture"
		EndIf

		If CurrentObject\Attributes\ModelName$="!Crab"
			tex2$="Status"
			If CurrentObject\Attributes\Data1=0
				tex$="Awake"
			Else If CurrentObject\Attributes\Data1=1
				tex$="Curious"
			Else If CurrentObject\Attributes\Data1=2
				tex$="Asleep"
			Else If CurrentObject\Attributes\Data1=3
				tex$="Disabled"
			EndIf
		EndIf

		; Q - player NPC functionality
		If IsModelNPC(CurrentObject\Attributes\ModelName$) Or CurrentObject\Attributes\LogicType=110
			tex2$="Expression"
			tex$=GetStinkerExpressionName$(CurrentObject\Attributes\Data1)
		EndIf

		; spring or bridge or transporter or gate or key or teleporter or cage or fire trap or laser gate or moobot or suctube or conveyor
		If CurrentObject\Attributes\LogicType=280 Or CurrentObject\Attributes\LogicType=40 Or CurrentObject\Attributes\LogicType=210 Or CurrentObject\Attributes\LogicType=10 Or CurrentObject\Attributes\LogicType=172 Or CurrentObject\Attributes\LogicType=30 Or CurrentObject\Attributes\LogicType=140 Or CurrentObject\Attributes\LogicType=20 Or CurrentObject\Attributes\LogicType=410 Or CurrentObject\Attributes\LogicType=424 Or CurrentObject\Attributes\LogicType=432 Or CurrentObject\Attributes\LogicType=281 Or CurrentObject\Attributes\LogicType=45 Or CurrentObject\Attributes\LogicType=46
			tex2$="SubColour"
		EndIf

		If CurrentObject\Attributes\LogicType=242 ; cuboid
			tex2$="Turning"
			If CurrentObject\Attributes\Data1=0
				tex$="No"
			Else
				tex$="Yes"
			EndIf
		EndIf

		If IsObjectTypeKeyblock(CurrentObject\Attributes\LogicType)
			tex2$=GetCMDData1Name$(CurrentObject\Attributes\Data0)
			tex$=GetCmdData1ValueName$(CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data1)
		EndIf

		If CurrentObject\Attributes\LogicType=90 ; button
			If IsObjectSubTypeFourColorButton(CurrentObject\Attributes\LogicSubType)
				tex2$="Colour2"
			Else If (CurrentObject\Attributes\LogicSubType Mod 32)<10 ; Color Changer
				tex2$="Col To"
			Else If (CurrentObject\Attributes\LogicSubType Mod 32)=15 ; General Command
				tex2$=GetCMDData1Name$(CurrentObject\Attributes\Data0)
				tex$=GetCmdData1ValueName$(CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data1)
			Else If (CurrentObject\Attributes\LogicSubType Mod 32)=16 Or (CurrentObject\Attributes\LogicSubType Mod 32)=17 ; Rotator or ???
				tex2$="SubColour"
			Else If CurrentObject\Attributes\LogicSubType = 10 ; LevelExit
				tex2$="Dest Level"
			Else If CurrentObject\Attributes\LogicSubType = 11 ; NPC Modifier
				If CurrentObject\Attributes\Data0=2 ; NPC Exclamation
					tex2$="Target ID"
					If CurrentObject\Attributes\Data1=-1
						tex$="Pla"
					EndIf
				Else
					tex2$="NPC ID"
				EndIf
			EndIf

		EndIf

		If CurrentObject\Attributes\LogicType=50 ; spellball
			tex2$="GoalY"
		EndIf
		If CurrentObject\Attributes\LogicType=190 ; Particle Emitter
			tex2$="Intensity"
			If CurrentObject\Attributes\Data1=1 tex$="Low"
			If CurrentObject\Attributes\Data1=2 tex$="Reg"
			If CurrentObject\Attributes\Data1=3 tex$="High"

		EndIf
		If CurrentObject\Attributes\LogicType=200 ; Glovecharge
			tex2$="Usability"
			If CurrentObject\Attributes\Data1<1
				tex$="Always"
			ElseIf CurrentObject\Attributes\Data1=1
				tex$="Once"
			ElseIf CurrentObject\Attributes\Data1>1
				tex$="Unusable"
			EndIf
			tex$=CurrentObject\Attributes\Data1+"/"+tex$
		EndIf
		If CurrentObject\Attributes\LogicType=11 ; TollGate
			tex2$="Type"
			If CurrentObject\Attributes\Data1=0
				tex$="Star"
			Else
				tex$="Coin"
			EndIf
			tex$=CurrentObject\Attributes\Data1+"/"+tex$

		EndIf
		If CurrentObject\Attributes\LogicType=230 ; FireFlower
			tex2$="Type"

			Select CurrentObject\Attributes\Data1
			Case 0
				tex$="Fire"
			Case 1
				tex$="Ice"
			Case 2
				tex$="Null"
			Case 3
				tex$="Bounce"
			End Select
		EndIf

		If CurrentObject\Attributes\LogicType=70 Or CurrentObject\Attributes\LogicType=179 ; Beta Pickup Item or Custom Item
			tex2$="Fn ID"
		EndIf

		If CurrentObject\Attributes\LogicType=260 ; Spikeyball
			tex2$="Type"
			If CurrentObject\Attributes\Data1=0
				tex$="Bounce Left"
			Else If CurrentObject\Attributes\Data1=1
				tex$="Bounce Right"
			Else
				tex$="Bounce Diag"
			EndIf
		EndIf
		If CurrentObject\Attributes\LogicType=250 ; Chomper
			tex2$="Special"
			If CurrentObject\Attributes\Data1=0
				tex$="---"
			Else If CurrentObject\Attributes\Data1=1
				tex$="Ghost"
			Else If CurrentObject\Attributes\Data1=2
				tex$="Glow"
			Else If CurrentObject\Attributes\Data1=3
				tex$="Mecha"
			EndIf
		EndIf
		If CurrentObject\Attributes\LogicType=220 ; Turtle
			tex2$="Turn"
			If CurrentObject\Attributes\Data1=0
				tex$="Left"
			Else If CurrentObject\Attributes\Data1=1
				tex$="Right"

			EndIf
		EndIf

		If CurrentObject\Attributes\LogicType=300 ; Brr float
			tex2$="TimeOffset"
		EndIf

		If CurrentObject\Attributes\LogicType=310 ; RubberDucky
			tex2$="TiltMagnitude"
		EndIf

		If CurrentObject\Attributes\LogicType=370 ; Crab
			tex2$="Status"
			If CurrentObject\Attributes\Data1=0
				tex$="Awake"
			Else If CurrentObject\Attributes\Data1=1
				tex$="Curious"
			Else If CurrentObject\Attributes\Data1=2
				tex$="Asleep"
			Else If CurrentObject\Attributes\Data1=3
				tex$="Disabled"
			EndIf
		EndIf

		If CurrentObject\Attributes\LogicType=290 Or CurrentObject\Attributes\LogicType=380 ; Thwart or Ice Troll
			tex2$="WalkAnim"
			If CurrentObject\Attributes\Data1=1
				tex$="Hands Up"
			Else
				tex$="Normal"
			EndIf
			tex$=CurrentObject\Attributes\Data1+"/"+tex$
		EndIf

		If CurrentObject\Attributes\LogicType=51 ; spellball generator
			tex2$="Goal X"
		EndIf

		; ufo or retro z-bot or zipbot or zapbot
		If CurrentObject\Attributes\LogicType=422 Or CurrentObject\Attributes\LogicType=423 Or CurrentObject\Attributes\LogicType=430 Or CurrentObject\Attributes\LogicType=431
			tex2$="Turning"
			If CurrentObject\Attributes\Data1=0
				tex$="Left"
			Else If CurrentObject\Attributes\Data1=1
				tex$="Right"
			EndIf
		EndIf

		If CurrentObject\Attributes\LogicType=460 ; BurstFlower
			tex2$="BurstProgress"
			If CurrentObject\Attributes\Data1=150
				tex$="Fire"
			EndIf
		EndIf

		If CurrentObject\Attributes\LogicType=470 ; Ghost
			tex2$="Speed"
		EndIf
		If CurrentObject\Attributes\LogicType=471 ; Wraith
			tex2$="ShotTime"
		EndIf

		If CurrentObject\Attributes\LogicType=52 ; meteor shooter
			tex2$="TargetX"
		EndIf

		tex$=SetAdjusterDisplayInt(ObjectAdjusterData1,CurrentObject\Attributes\Data1,tex$)

	Case "Data2"
		tex$=Str$(CurrentObject\Attributes\Data2)

		If CurrentObject\Attributes\ModelName$="!ColourGate"
			tex2$="Frame"
		EndIf

		If CurrentObject\Attributes\ModelName$="!FlipBridge"
			tex2$="Direction"
			tex$=GetFlipbridgeDirectionName$(CurrentObject\Attributes\Data2)
		EndIf

		If CurrentObject\Attributes\ModelName$="!Gem"
			tex2$="XOffset"
		EndIf

		; Q - player NPC functionality
		If IsModelNPC(CurrentObject\Attributes\ModelName$)
			tex2$="Acc1"

			tex$=GetAccessoryName$(CurrentObject\Attributes\Data2)

			tex$=CurrentObject\Attributes\Data2+"/"+tex$

		EndIf

		If CurrentObject\Attributes\ModelName$="!Thwart"
			tex2$="Colour"
			If CurrentObject\Attributes\Data2=0
				tex$="Standard"
			Else If CurrentObject\Attributes\Data2=1
				tex$="Red"
			Else If CurrentObject\Attributes\Data2=2
				tex$="Orange"
			Else If CurrentObject\Attributes\Data2=3
				tex$="Yellow"
			Else If CurrentObject\Attributes\Data2=4
				tex$="Green"
			Else If CurrentObject\Attributes\Data2=5
				tex$="Blue"
			Else If CurrentObject\Attributes\Data2=6
				tex$="Indigo"
			Else If CurrentObject\Attributes\Data2=7
				tex$="Purple"

			EndIf
		EndIf

		If CurrentObject\Attributes\ModelName$="!Sun Sphere1"
			tex2$="Blue"
		EndIf

		If CurrentObject\Attributes\ModelName$="!Wraith"
			tex2$="Magic"
			Select CurrentObject\Attributes\Data2
			Case 0
				tex$="Fire"
			Case 1
				tex$="Ice"
			Case 2
				tex$="Grow"
			Default
				tex$="None"
			End Select
			tex$=CurrentObject\Attributes\Data2+"/"+tex$
		EndIf

		If CurrentObject\Attributes\ModelName="!Suctube"
			tex2$="Direction"
			tex$=GetDirectionString$(CurrentObject\Attributes\Data2*90)
		EndIf

		If CurrentObject\Attributes\LogicType=280 ; Spring
			tex2$="Direction"
			tex$=GetDirectionString$(CurrentObject\Attributes\Data2*45)
		EndIf

		If CurrentObject\Attributes\LogicType=281 ; Suctube -
			tex2$="Direction"
			tex$=GetDirectionString$(CurrentObject\Attributes\Data2*90)
		EndIf

		; transporter or suctubex or conveyor head/tail
		If CurrentObject\Attributes\LogicType=210 Or CurrentObject\Attributes\LogicType=282 Or CurrentObject\Attributes\LogicType=45 Or CurrentObject\Attributes\LogicType=46
			tex2$="Direction"
			If CurrentObject\Attributes\LogicType=210 ; transporter
				DirectionVal=3-CurrentObject\Attributes\Data2
				;tex$=Str$(DirectionVal)
				Select DirectionVal
				Case 0
					tex$="East"
				Case 1
					tex$="South"
				Case 2
					tex$="West"
				Case 3
					tex$="North"
				End Select
			EndIf
		EndIf

		If CurrentObject\Attributes\LogicType=410 ; Flipbridge
			tex2$="Direction"
			tex$=GetFlipbridgeDirectionName$(CurrentObject\Attributes\Data2)
		EndIf

		If CurrentObject\Attributes\LogicType=432 ; Moobot
			tex2$="Direction"
			Select CurrentObject\Attributes\Data2
			Case 0
				tex$="North"
			Case 1
				tex$="East"
			Case 2
				tex$="South"
			Case 3
				tex$="West"
			Default
				tex$=CurrentObject\Attributes\Data2+"/NoMove"
			End Select
		EndIf

		If CurrentObject\Attributes\LogicType=160 And CurrentObject\Attributes\ModelName$="!CustomModel"
			tex2$="RollAnim"
		EndIf

		If CurrentObject\Attributes\LogicType=471 ; Wraith
			tex2$="Magic"
			Select CurrentObject\Attributes\Data2
			Case 0
				tex$="Fire"
			Case 1
				tex$="Ice"
			Case 2
				tex$="Grow"
			Default
				tex$="None"
			End Select
			tex$=CurrentObject\Attributes\Data2+"/"+tex$
		EndIf

		If IsObjectTypeKeyblock(CurrentObject\Attributes\LogicType)
			tex2$=GetCMDData2Name$(CurrentObject\Attributes\Data0)
			tex$=GetCmdData2ValueName$(CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data2)
		EndIf

		If CurrentObject\Attributes\LogicType=90 ; button
			If IsObjectSubTypeFourColorButton(CurrentObject\Attributes\LogicSubType)
				tex2$="Colour3"
			Else If (CurrentObject\Attributes\LogicSubType Mod 32)<10 ; Color Changer
				tex2$="SubCol From"
			Else If (CurrentObject\Attributes\LogicSubType Mod 32)=15 ; General Command
				tex2$=GetCMDData2Name$(CurrentObject\Attributes\Data0)
				tex$=GetCmdData2ValueName$(CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data2)
			Else If (CurrentObject\Attributes\LogicSubType Mod 32)=16 Or (CurrentObject\Attributes\LogicSubType Mod 32)=17 ; Rotator or ???
				tex2$="Direction"
			Else If CurrentObject\Attributes\LogicSubType = 10 ; LevelExit
				tex2$="Dest X"
			Else If CurrentObject\Attributes\LogicSubType = 11 And CurrentObject\Attributes\Data0=0 ; NPC Move
				tex2$="X Goal"
			Else If CurrentObject\Attributes\LogicSubType = 11 And CurrentObject\Attributes\Data0=1 ; NPC Change
				tex2$="Dialog"
				If CurrentObject\Attributes\Data2=0 Then tex$="None"
				If CurrentObject\Attributes\Data2=-1 Then	tex$="No Change"
			Else If CurrentObject\Attributes\LogicSubType = 11 And CurrentObject\Attributes\Data0=2 ; NPC Exclamation
				tex2$="Particle ID"
			EndIf
		EndIf

		If CurrentObject\Attributes\LogicType=50 ; spellball
			tex2$="SourceX"
		EndIf

		If CurrentObject\Attributes\LogicType=70 ; Beta Pickup Item
			tex2$="Texture"
		EndIf

		If CurrentObject\Attributes\LogicType=190
			tex2$="Direction"
			If CurrentObject\Attributes\Data2=0 tex$="Up"
			If CurrentObject\Attributes\Data2=1 tex$="Down"
			If CurrentObject\Attributes\Data2=2 tex$="East"
			If CurrentObject\Attributes\Data2=3 tex$="West"
			If CurrentObject\Attributes\Data2=4 tex$="North"
			If CurrentObject\Attributes\Data2=5 tex$="South"

		EndIf

		If CurrentObject\Attributes\LogicType=230 ; FireFlower
			tex2$="State"
		EndIf

		If CurrentObject\Attributes\LogicType=260 ; Spikeyball
			tex2$="Speed"
			tex$="+"+Str$(CurrentObject\Attributes\Data2)
		EndIf

		If CurrentObject\Attributes\LogicType=300 ; Brr float
			tex2$="PitchAnim"
		EndIf

		If CurrentObject\Attributes\LogicType=301 ; Rainbow float
			tex2$="ColorOffset"
			If CurrentObject\Attributes\Data2=0
				tex$="Random"
			EndIf
		EndIf

		If CurrentObject\Attributes\LogicType=310 ; RubberDucky
			tex2$="TimeOffset"
		EndIf

		If CurrentObject\Attributes\LogicType=320 ; Void
			tex2$="SizeAdjust"
		EndIf

		If CurrentObject\Attributes\LogicType=433 ; Z-Bot NPC
			tex2$="Turning"
			If CurrentObject\Attributes\Data2=0
				tex$="Player"
			Else
				tex$="Fixed"
			EndIf
			tex$=CurrentObject\Attributes\Data2+"/"+tex$
		EndIf

		If CurrentObject\Attributes\LogicType=180 ; Sign
			tex2$="Move"

			Select CurrentObject\Attributes\Data2
			Case 0
				tex$="No"
			Case 1
				tex$="Sway"
			Case 2
				tex$="Bounce"
			Case 3
				tex$="Turn"
			End Select

		EndIf
		If CurrentObject\Attributes\LogicType=51 ; spellball generator
			tex2$="Goal Y"
		EndIf

		If CurrentObject\Attributes\LogicType=431 ; zapbot
			tex2$="Speed"
		EndIf

		If CurrentObject\Attributes\LogicType=242 ; cuboid
			tex2$="CMD" ;"Explo CMD"
			tex$=CurrentObject\Attributes\Data2+"/"+GetCommandName$(CurrentObject\Attributes\Data2)
		EndIf

		If CurrentObject\Attributes\LogicType=434 ; mothership
			tex2$="SourceX"
		EndIf

		If CurrentObject\Attributes\LogicType=52 ; meteor shooter
			tex2$="TargetY"
		EndIf

		tex$=SetAdjusterDisplayInt(ObjectAdjusterData2,CurrentObject\Attributes\Data2,tex$)

	Case "Data3"
		tex$=Str$(CurrentObject\Attributes\Data3)

		If CurrentObject\Attributes\ModelName$="!Suctube" Or CurrentObject\Attributes\ModelName$="!SuctubeX"
			tex2$="Style"
		EndIf

		If CurrentObject\Attributes\ModelName$="!IceBlock"
			tex2$="Style"
			Select CurrentObject\Attributes\Data3
			Case 0
				tex$="Ice"
			Case 1
				tex$="Floing"
			End Select
		EndIf

		; Q - player NPC functionality
		If IsModelNPC(CurrentObject\Attributes\ModelName$)
			tex2$="Colour1"
			tex$=GetAccessoryColorNameWithColorInt$(CurrentObject\Attributes\Data2,CurrentObject\Attributes\Data3)
		EndIf

		If CurrentObject\Attributes\LogicType=432 Or CurrentObject\Attributes\ModelName$="!Pushbot" ; Moobots
			tex2$="Turn"
			If CurrentObject\Attributes\Data3=2
				tex$="180"
			Else
				If CurrentObject\Attributes\Data3 Mod 2=0
					tex$="Left"
				Else
					If CurrentObject\Attributes\Data3<0
						tex$="NoMove"
					Else
						tex$="Right"
					EndIf
				EndIf
			EndIf
			tex$=CurrentObject\Attributes\Data3+"/"+tex$
		EndIf

		If CurrentObject\Attributes\LogicType=160 And CurrentObject\Attributes\ModelName$="!CustomModel"
			tex2$="XAnim"
		EndIf

		If CurrentObject\Attributes\LogicType=45 ; Conveyor heads
			tex2$="Turn"
			If CurrentObject\Attributes\Data3=0
				tex$="Left"
			Else If CurrentObject\Attributes\Data3=1
				tex$="Right"
			EndIf
		EndIf

		If CurrentObject\Attributes\LogicType=210 ; Transporters
			tex2$="Turn"
			If CurrentObject\Attributes\Data3=0
				tex$="180"
			Else If CurrentObject\Attributes\Data3=1
				tex$="(MOD) Left"
			Else If CurrentObject\Attributes\Data3=2
				tex$="(MOD) Right"
			EndIf
		EndIf

		If CurrentObject\Attributes\LogicType=46 ; conveyor tail
			tex2$="Cycles"
		EndIf
		If CurrentObject\Attributes\LogicType=40 ; stepping stone
			tex2$="Sound"
			If CurrentObject\Attributes\Data3=0
				tex$="Water"
			Else If CurrentObject\Attributes\Data3=1
				tex$="Mecha"
			Else If CurrentObject\Attributes\Data3=2
				tex$="Magic"
			Else
				tex$="Silent/Glitched"
			EndIf
		EndIf

		If CurrentObject\Attributes\LogicType=50 ; spellball
			tex2$="SourceY"
		EndIf
		If CurrentObject\Attributes\LogicType=190 ; Particle Spawner
			If CurrentObject\Attributes\LogicSubType=4 Or CurrentObject\Attributes\LogicSubType=5
				tex2$="Sound"
				If CurrentObject\Attributes\Data3=0 tex$="None"
			EndIf

			If CurrentObject\Attributes\Data3=1
				If CurrentObject\Attributes\LogicSubType=4 tex$="Spark"
				If CurrentObject\Attributes\LogicSubType=5 tex$="QuietMagic"
			EndIf
			If CurrentObject\Attributes\Data3=2
				If CurrentObject\Attributes\LogicSubType=5 tex$="LoudMecha"
			EndIf
			If CurrentObject\Attributes\Data3=3
				If CurrentObject\Attributes\LogicSubType=5 tex$="Var.Gong"
			EndIf
			If CurrentObject\Attributes\Data3=4
				If CurrentObject\Attributes\LogicSubType=5 tex$="Grow Magic"
			EndIf
			If CurrentObject\Attributes\Data3=5
				If CurrentObject\Attributes\LogicSubType=5 tex$="Floing Magic"
			EndIf
			If CurrentObject\Attributes\Data3=6
				If CurrentObject\Attributes\LogicSubType=5 tex$="Gem"
			EndIf
		EndIf

		If IsObjectTypeKeyblock(CurrentObject\Attributes\LogicType)
			tex2$=GetCMDData3Name$(CurrentObject\Attributes\Data0)
			tex$=GetCmdData3ValueName$(CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data2,CurrentObject\Attributes\Data3)
		EndIf

		If CurrentObject\Attributes\LogicType=90 ; button
			If IsObjectSubTypeFourColorButton(CurrentObject\Attributes\LogicSubType)
				tex2$="Colour4"
			Else If (CurrentObject\Attributes\LogicSubType Mod 32)<10 ; Color Changer
				tex2$="SubCol To"
			Else If CurrentObject\Attributes\LogicSubType = 11 And CurrentObject\Attributes\Data0=0 ; NPC Modifier: NPC Move
				tex2$="Y Goal"
			Else If CurrentObject\Attributes\LogicSubType = 10 ; LevelExit
				tex2$="Dest Y"

			Else If CurrentObject\Attributes\LogicSubType = 11 And CurrentObject\Attributes\Data0=1 ; NPC Modifier: NPC Change
				tex2$="Expression"
				If CurrentObject\Attributes\Data3=-1
					tex$="No Change"
				Else
					tex$=GetStinkerExpressionName$(CurrentObject\Attributes\Data3)
				EndIf
			Else If CurrentObject\Attributes\LogicSubType = 11 And CurrentObject\Attributes\Data0=2 ; NPC Modifier: NPC Exclamation
				tex2$="Count"
			Else If (CurrentObject\Attributes\LogicSubType Mod 32)=15 ; General Command
				tex2$=GetCMDData3Name$(CurrentObject\Attributes\Data0)
				tex$=GetCmdData3ValueName$(CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data2,CurrentObject\Attributes\Data3)
			EndIf
		EndIf

		If CurrentObject\Attributes\LogicType=230 ; FireFlower
			tex2$="HitPoints"
		EndIf

		If CurrentObject\Attributes\LogicType=300 ; Brr float
			tex2$="RollAnim"
		EndIf

		If  CurrentObject\Attributes\LogicType=431 ; Zapbot
			tex2$="Range"
		EndIf

		If  CurrentObject\Attributes\LogicType=242 ; Cuboid
			;tex2$="Cmd Data1"
			tex2$=GetCMDData1Name$(CurrentObject\Attributes\Data2)
			tex$=GetCmdData1ValueName$(CurrentObject\Attributes\Data2,CurrentObject\Attributes\Data3)
		EndIf

		If CurrentObject\Attributes\LogicType=434 ; Mothership
			tex2$="SourceY"
		EndIf

		If CurrentObject\Attributes\LogicType=52 ; meteor shooter
			tex2$="TargetZ"
		EndIf

		tex$=SetAdjusterDisplayInt(ObjectAdjusterData3,CurrentObject\Attributes\Data3,tex$)

	Case "Data4"
		tex$=Str$(CurrentObject\Attributes\Data4)

		If CurrentObject\Attributes\LogicType=160 And CurrentObject\Attributes\ModelName$="!CustomModel"
			tex2$="YAnim"
		EndIf

		If CurrentObject\Attributes\ModelName$="!Conveyor"
			tex2$="Visual Type"
		EndIf

		; Q - player NPC functionality
		If IsModelNPC(CurrentObject\Attributes\ModelName$)
			tex2$="Acc2" ;"Glasses"

			tex$=GetAccessoryName$(CurrentObject\Attributes\Data4)

			tex$=CurrentObject\Attributes\Data4+"/"+tex$
		EndIf

		If IsObjectTypeKeyblock(CurrentObject\Attributes\LogicType)
			tex2$=GetCMDData4Name$(CurrentObject\Attributes\Data0)
			tex$=GetCmdData4ValueName$(CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data4)
		EndIf

		If CurrentObject\Attributes\LogicType=90 ; button
			If IsObjectSubTypeFourColorButton(CurrentObject\Attributes\LogicSubType)
				tex2$="SubColour1"

			Else If CurrentObject\Attributes\LogicSubType=10 ; LevelExit
				tex2$="PlayerYaw"
				DisplayedRotation=(CurrentObject\Attributes\Data4+180) Mod 360
				tex$=GetDirectionString$(DisplayedRotation)

			Else If CurrentObject\Attributes\LogicSubType=11 And (CurrentObject\Attributes\Data0=0 Or CurrentObject\Attributes\Data0=2) ; NPC Modifier: NPC Move or NPC Exclamation
				tex2$="Repeatable"
				If CurrentObject\Attributes\Data4=0
					tex$="Yes"
				Else
					tex$="No"
				EndIf
			Else If CurrentObject\Attributes\LogicSubType=11 And CurrentObject\Attributes\Data0=1 ; NPC Modifier: NPC Change
				tex2$="Yaw"
				If CurrentObject\Attributes\Data4=-1
					tex$="No Change"
				Else
					;tex$=GetDirectionString$(CurrentObject\Attributes\Data4)
					tex$=CurrentObject\Attributes\Data4
				EndIf
			Else If (CurrentObject\Attributes\LogicSubType Mod 32)=15 ; General Command
				tex2$=GetCMDData4Name$(CurrentObject\Attributes\Data0)
				tex$=GetCmdData4ValueName$(CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data4)
			EndIf
		EndIf

		If CurrentObject\Attributes\LogicType=281 ; suctube
			tex2$="Sound"
			If CurrentObject\Attributes\Data4=0
				tex$="Normal"
			Else
				tex$="Portal"
			EndIf
		EndIf

		If CurrentObject\Attributes\LogicType=190 ; Particle Emitter
			tex2$="Timing"
			If CurrentObject\Attributes\Data4=0 tex$="Random"
			If CurrentObject\Attributes\Data4=1 tex$="Synchro"
		EndIf

		If CurrentObject\Attributes\LogicType=230 ; FireFlower
			tex2$="PreviousHP"
		EndIf

		If CurrentObject\Attributes\LogicType=10 And CurrentObject\Attributes\LogicSubType=9 ; Autodoor
			tex2$=GetAutodoorActivateName$(CurrentObject\Attributes\Data4)
			tex$=GetAutodoorActivateValueName$(CurrentObject\Attributes\Data4)
		EndIf

		If CurrentObject\Attributes\LogicType=200 ; Glovecharge
			tex2$="(MOD) Homing"
			tex$=CurrentObject\Attributes\Data4+"/"+OneToYes$(CurrentObject\Attributes\Data4)
		EndIf

		If CurrentObject\Attributes\LogicType=242 ; Cuboid
			;tex2$="Cmd Data2"
			tex2$=GetCMDData2Name$(CurrentObject\Attributes\Data2)
			tex$=GetCmdData2ValueName$(CurrentObject\Attributes\Data2,CurrentObject\Attributes\Data4)
		EndIf

		If CurrentObject\Attributes\LogicType=431 Or CurrentObject\Attributes\LogicType=422 ; Zapbot or UFO
			tex2$="Track"
			tex$=CurrentObject\Attributes\Data4+"/"+OneToYes$(CurrentObject\Attributes\Data4)
		EndIf

		If CurrentObject\Attributes\LogicType=434 ; Mothership
			tex2$="FlyGoalX1"
		EndIf

		tex$=SetAdjusterDisplayInt(ObjectAdjusterData4,CurrentObject\Attributes\Data4,tex$)

	Case "Data5"
		tex$=Str$(CurrentObject\Attributes\Data5)

		; Q - player NPC functionality
		If IsModelNPC(CurrentObject\Attributes\ModelName$)
			tex2$="Colour2"
			tex$=GetAccessoryColorNameWithColorInt$(CurrentObject\Attributes\Data4,CurrentObject\Attributes\Data5+1)
		EndIf

		CurrentObjectModelName$=CurrentObject\Attributes\ModelName$
		If CurrentObjectModelName$="!Door" Or CurrentObjectModelName$="!Obstacle36" Or CurrentObjectModelName$="!Obstacle37" Or CurrentObjectModelName$="!Obstacle38" Or CurrentObjectModelName$="!Obstacle39" Or CurrentObjectModelName$="!Obstacle40"
			tex2$="Style"
		EndIf

		If CurrentObject\Attributes\ModelName$="!GlowWorm" Or CurrentObject\Attributes\ModelName$="!Zipper"
			tex2$="Red"
		EndIf

		If CurrentObject\Attributes\LogicType=160 And CurrentObject\Attributes\ModelName$="!CustomModel"
			tex2$="ZAnim"
		EndIf

		If CurrentObject\Attributes\LogicType=281 ; Suctube
			tex2$="Particles"
			If CurrentObject\Attributes\Data5=0
				tex$="Yes"
			Else
				tex$="No"
			EndIf
		EndIf

		If CurrentObject\Attributes\LogicType=90 ; button
			If IsObjectSubTypeFourColorButton(CurrentObject\Attributes\LogicSubType)
				tex2$="SubColour2"
			Else If CurrentObject\Attributes\LogicSubType = 10
				tex2$="FlyOver"
				If CurrentObject\Attributes\Data5=0
					tex$="No"
				Else
					tex$="Yes"
				EndIf

			Else If (CurrentObject\Attributes\LogicSubType Mod 32)=15 Or (CurrentObject\Attributes\LogicSubType = 11 And CurrentObject\Attributes\Data0=1)
				tex2$="Repeatable"
				If CurrentObject\Attributes\Data5=0
					tex$="Yes"
				Else
					tex$="No"
				EndIf
			Else If CurrentObject\Attributes\LogicSubType = 11 And CurrentObject\Attributes\Data0=0
				tex2$="DelayTimer"

			EndIf
		EndIf

		If CurrentObject\Attributes\LogicType=45 Or CurrentObject\Attributes\LogicType=46 ; Conveyor (should the tail really be here too?)
			tex2$="Logic"
			If CurrentObject\Attributes\Data5=0
				tex$="Move"
			Else
				tex$="Step"
			EndIf
		EndIf

		If CurrentObject\Attributes\LogicType=10 And CurrentObject\Attributes\LogicSubType=9 ; Autodoor
			tex2$=GetAutodoorActivateName$(CurrentObject\Attributes\Data5)
			tex$=GetAutodoorActivateValueName$(CurrentObject\Attributes\Data5)
		EndIf

		If CurrentObject\Attributes\LogicType=242 ; Cuboid
			;tex2$="Cmd Data3"
			tex2$=GetCMDData3Name$(CurrentObject\Attributes\Data2)
			tex$=GetCmdData3ValueName$(CurrentObject\Attributes\Data2,CurrentObject\Attributes\Data4,CurrentObject\Attributes\Data5)
		EndIf

		If CurrentObject\Attributes\LogicType=434 ; Mothership
			tex2$="FlyGoalY1"
		EndIf

		tex$=SetAdjusterDisplayInt(ObjectAdjusterData5,CurrentObject\Attributes\Data5,tex$)

	Case "Data6"
		tex$=Str$(CurrentObject\Attributes\Data6)

		If CurrentObject\Attributes\ModelName$="!GlowWorm" Or CurrentObject\Attributes\ModelName$="!Zipper"
			tex2$="Green"
		EndIf

		If CurrentObject\Attributes\LogicType=160 And CurrentObject\Attributes\ModelName$="!CustomModel"
			tex2$="XSpeed"
		EndIf

		If CurrentObject\Attributes\LogicType=90 ; button
			If IsObjectSubTypeFourColorButton(CurrentObject\Attributes\LogicSubType)
				tex2$="SubColour3"
			Else If CurrentObject\Attributes\LogicSubType = 11 And CurrentObject\Attributes\Data0=0 ; NPC Modifier: NPC Move
				tex2$="DelayReset"
			Else If CurrentObject\Attributes\LogicSubType = 11 And CurrentObject\Attributes\Data0=1 ; NPC Modifier: NPC Change
				tex2$="WalkAnim"
				If CurrentObject\Attributes\Data6=-1
					tex$="No Change"
				Else
					tex$=GetStinkerNPCWalkAnimName$(CurrentObject\Attributes\Data6)
				EndIf
			EndIf
		EndIf

		If CurrentObject\Attributes\LogicType=110 ; Stinker NPC
			tex2$="WalkAnim"
			tex$=GetStinkerNPCWalkAnimName$(CurrentObject\Attributes\Data6)
		EndIf

		If CurrentObject\Attributes\LogicType=120 ; Wee Stinker
			tex2$="Burning"
			If CurrentObject\Attributes\Data6=600 tex$="Death"
		EndIf

		; Thwart, Ice Troll, Z-Bot NPC
		If CurrentObject\Attributes\LogicType=290 Or CurrentObject\Attributes\LogicType=380 Or CurrentObject\Attributes\LogicType=433
			tex2$="Shooter"
			If CurrentObject\Attributes\Data6>0
				tex$="Yes"
			Else
				tex$="No"
			EndIf
			tex$=CurrentObject\Attributes\Data6+"/"+tex$
		EndIf

		If CurrentObject\Attributes\LogicType=10 And CurrentObject\Attributes\LogicSubType=9 ; Autodoor
			tex2$=GetAutodoorActivateName$(CurrentObject\Attributes\Data6)
			tex$=GetAutodoorActivateValueName$(CurrentObject\Attributes\Data6)
		EndIf
		If CurrentObject\Attributes\LogicType=45 Or CurrentObject\Attributes\LogicType=46 ; Conveyor (is tail relevant here?)
			tex2$="ActivationWait"
		EndIf

		If CurrentObject\Attributes\LogicType=242 ; Cuboid
			;tex2$="Cmd Data4"
			tex2$=GetCMDData4Name$(CurrentObject\Attributes\Data2)
			tex$=GetCmdData4ValueName$(CurrentObject\Attributes\Data2,CurrentObject\Attributes\Data6)
		EndIf

		If CurrentObject\Attributes\LogicType=434 ; Mothership
			tex2$="FlyGoalX2"
		EndIf

		tex$=SetAdjusterDisplayInt(ObjectAdjusterData6,CurrentObject\Attributes\Data6,tex$)

	Case "Data7"
		tex$=Str$(CurrentObject\Attributes\Data7)

		If CurrentObject\Attributes\ModelName$="!GlowWorm"  Or CurrentObject\Attributes\ModelName$="!Zipper"
			tex2$="Blue"
		EndIf

		If CurrentObject\Attributes\LogicType=160 And CurrentObject\Attributes\ModelName$="!CustomModel"
			tex2$="YSpeed"
		EndIf

		If CurrentObject\Attributes\LogicType=90 ; button
			If IsObjectSubTypeFourColorButton(CurrentObject\Attributes\LogicSubType)
				tex2$="SubColour4"
			Else If CurrentObject\Attributes\LogicSubType = 11 And CurrentObject\Attributes\Data0=1 ; NPC Modifier: NPC Change
				tex2$="Turn"
				If CurrentObject\Attributes\Data7=-1
					tex$="No Change"
				Else
					tex$=GetNPCTurningName$(CurrentObject\Attributes\Data7)
				EndIf

			EndIf
		EndIf
		If CurrentObject\Attributes\LogicType=110 Or CurrentObject\Attributes\LogicType=390 ; Stinker NPC or Kaboom NPC

			tex2$="Turn"
			tex$=GetNPCTurningName$(CurrentObject\Attributes\Data7)
		EndIf

		If CurrentObject\Attributes\LogicType=290 Or CurrentObject\Attributes\LogicType=380 Or CurrentObject\Attributes\LogicType=433 ; Thwart, Ice Troll, and Z-Bot NPC

			tex2$="AttackTimer" ; "TimerMax1"
		EndIf

		If CurrentObject\Attributes\LogicType=10 And CurrentObject\Attributes\LogicSubType=9 ; Autodoor
			tex2$="StayOnTimer"

		EndIf

		If CurrentObject\Attributes\LogicType=434 ; Mothership
			tex2$="FlyGoalY2"
		EndIf

		If CurrentObject\Attributes\LogicType=441 ; Sun Sphere 1
			tex2$="TimeOffset"
		EndIf

		tex$=SetAdjusterDisplayInt(ObjectAdjusterData7,CurrentObject\Attributes\Data7,tex$)

	Case "Data8"
		tex$=Str$(CurrentObject\Attributes\Data8)

		If CurrentObject\Attributes\LogicType=160 And CurrentObject\Attributes\ModelName$="!CustomModel"
			tex2$="ZSpeed"
		EndIf

		If CurrentObject\Attributes\LogicType=120 ; Wee Stinker
			tex2$="Type"
			If CurrentObject\Attributes\Data8=0 tex$="Normal"
			If CurrentObject\Attributes\Data8=1 tex$="Green"
			If CurrentObject\Attributes\Data8=2 tex$="White"
		EndIf

		If CurrentObject\Attributes\LogicType=90 Or CurrentObject\Attributes\LogicType=210 ; button or transporter
			tex2$="ActivateID"
			If CurrentObject\Attributes\Data8=0
				tex$="All"
			Else If CurrentObject\Attributes\Data8=-2
				tex$="Pla"
			EndIf
		EndIf
		If CurrentObject\Attributes\LogicType=110 ; Stinker NPC

			tex2$="IdleAnim"
			tex$=GetStinkerNPCIdleAnimName$(CurrentObject\Attributes\Data8)

		EndIf

		If CurrentObject\Attributes\LogicType=390 ; Kaboom NPC

			tex2$="Anim"
			If CurrentObject\Attributes\Data8=0 tex$="Stand"
			If CurrentObject\Attributes\Data8=1 tex$="Sit"
			If CurrentObject\Attributes\Data8=2 tex$="Sit/Stand"
			If CurrentObject\Attributes\Data8=3 tex$="Shiver Some"
			If CurrentObject\Attributes\Data8=4 tex$="Shiver Constant"
			If CurrentObject\Attributes\Data8=5 tex$="Exercise"

		EndIf

		If CurrentObject\Attributes\LogicType=400 ; Baby Boomer

			tex2$="Boom"
			If CurrentObject\Attributes\Data8=0 tex$="No"
			If CurrentObject\Attributes\Data8=1 tex$="Yes"

		EndIf

		If CurrentObject\Attributes\LogicType=50 ; spellball
			tex2$="FromZapbot"
			If CurrentObject\Attributes\Data8=0 tex$="No"
			If CurrentObject\Attributes\Data8=-99 tex$="Yes"
		EndIf

		If CurrentObject\Attributes\LogicType=422 Or CurrentObject\Attributes\LogicType=431 ; UFO or Zapbot
			tex2$="LastShotTimer"
		EndIf

		If CurrentObject\Attributes\LogicType=434 ; Mothership
			tex2$="FlyGoalX3"
		EndIf

		If CurrentObject\Attributes\LogicType=471 ; Wraith
			tex2$="TimeOffset"
		EndIf

		tex$=SetAdjusterDisplayInt(ObjectAdjusterData8,CurrentObject\Attributes\Data8,tex$)

	Case "Data9"
		tex$=Str$(CurrentObject\Attributes\Data9)

		If CurrentObject\Attributes\LogicType=160 And CurrentObject\Attributes\ModelName$="!CustomModel"
			tex2$="Deadly"
			If CurrentObject\Attributes\Data9=0 tex$="No"
			If CurrentObject\Attributes\Data9=1 tex$="Yes"

		EndIf

		If CurrentObject\Attributes\LogicType=90 ; button
			If CurrentObject\Attributes\LogicSubType = 11 And CurrentObject\Attributes\Data0=1 ; NPC Modifier: NPC Change
				tex2$="IdleAnim"
				If CurrentObject\Attributes\Data9=-1
					tex$="No Change"
				Else
					tex$=GetStinkerNPCIdleAnimName$(CurrentObject\Attributes\Data9)
				EndIf
			EndIf
		EndIf

		If CurrentObject\Attributes\LogicType=45 Or CurrentObject\Attributes\LogicType=46 ; Conveyor head (or conveyor tail, assuming this is needed?)
			tex2$="Tail Length"
		EndIf

		If CurrentObject\Attributes\LogicType=200
			tex2$="ReadyForSound"
			tex$=CurrentObject\Attributes\Data9+"/"+ZeroToYes$(CurrentObject\Attributes\Data9)
		EndIf

		If CurrentObject\Attributes\LogicType=422 Or CurrentObject\Attributes\LogicType=430 Or CurrentObject\Attributes\LogicType=431 ; UFO or Zipbot or Zapbot
			tex2$="TrackTextureID"
			If CurrentObject\Attributes\Data9=-1
				tex$="Current"
			EndIf
		EndIf

		If CurrentObject\Attributes\LogicType=434 ; Mothership
			tex2$="FlyGoalY3"
		EndIf

		If CurrentObject\Attributes\LogicType=441 ; Sun Sphere 1
			tex2$="Empty"
			If CurrentObject\Attributes\Data9=0 tex$="No"
			If CurrentObject\Attributes\Data9=1 tex$="Yes"
		EndIf

		If CurrentObject\Attributes\LogicType=470 Or CurrentObject\Attributes\LogicType=471 ; Ghost or Wraith
			tex2$="Visibility"
		EndIf

		tex$=SetAdjusterDisplayInt(ObjectAdjusterData9,CurrentObject\Attributes\Data9,tex$)

	Case "Talkable"
		tex2$="Dialog"
		If CurrentObject\Attributes\Talkable=0
			tex$="None"
		Else
			tex$=Str$(CurrentObject\Attributes\Talkable)
		EndIf

		tex$=SetAdjusterDisplayInt(ObjectAdjusterTalkable,CurrentObject\Attributes\Talkable,tex$)

	Case "MovementType"
		tex$=CurrentObject\Attributes\MovementType+"/"+GetMovementTypeString$(CurrentObject\Attributes\MovementType)
		tex2$="MvmtType"
		tex$=SetAdjusterDisplayInt(ObjectAdjusterMovementType,CurrentObject\Attributes\MovementType,tex$)
	Case "MovementTypeData"
		tex$=Str$(CurrentObject\Attributes\MovementTypeData)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterMovementTypeData,CurrentObject\Attributes\MovementTypeData,tex$)

	Case "MovementSpeed"
		tex$=Str$(CurrentObject\Attributes\MovementSpeed)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterMovementSpeed,CurrentObject\Attributes\MovementSpeed,tex$)

	Case "TileTypeCollision"
		tex$=DisplayAsBinaryString$(CurrentObject\Attributes\TileTypeCollision)
		tex2$="TTC"
		tex$=SetAdjusterDisplayInt(ObjectAdjusterTileTypeCollision,CurrentObject\Attributes\TileTypeCollision,tex$)

	Case "ObjectTypeCollision"
		tex$=DisplayAsBinaryString$(CurrentObject\Attributes\ObjectTypeCollision)
		tex2$="OTC"
		tex$=SetAdjusterDisplayInt(ObjectAdjusterObjectTypeCollision,CurrentObject\Attributes\ObjectTypeCollision,tex$)

	Case "ScaleAdjust"
		tex$=Str$(CurrentObject\Attributes\ScaleAdjust)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterScaleAdjust,CurrentObject\Attributes\ScaleAdjust,tex$)
	Case "Exclamation"
		If CurrentObject\Attributes\Exclamation=-1
			tex$="None"
		Else
			tex$=Str$(CurrentObject\Attributes\Exclamation)
		EndIf

		tex$=SetAdjusterDisplayInt(ObjectAdjusterExclamation,CurrentObject\Attributes\Exclamation,tex$)

	Case "Linked"
		If CurrentObject\Attributes\Linked=-1
			tex$="None"
		ElseIf CurrentObject\Attributes\Linked=-2
			tex$="Pla"
		Else
			tex$=Str$(CurrentObject\Attributes\Linked)
		EndIf

		tex$=SetAdjusterDisplayInt(ObjectAdjusterLinked,CurrentObject\Attributes\Linked,tex$)

	Case "LinkBack"
		If CurrentObject\Attributes\LinkBack=-1
			tex$="None"
		ElseIf CurrentObject\Attributes\LinkBack=-2
			tex$="Pla"
		Else
			tex$=Str$(CurrentObject\Attributes\LinkBack)
		EndIf

		tex$=SetAdjusterDisplayInt(ObjectAdjusterLinkBack,CurrentObject\Attributes\LinkBack,tex$)

	Case "Parent"
		tex$=Str$(CurrentObject\Attributes\Parent)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterParent,CurrentObject\Attributes\Parent,tex$)

	Case "Child"
		tex$=Str$(CurrentObject\Attributes\Child)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterChild,CurrentObject\Attributes\Child,tex$)

	Case "DX"
		tex$=Str$(CurrentObject\Attributes\DX)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterDX,CurrentObject\Attributes\DX,tex$)

	Case "DY"
		tex$=Str$(CurrentObject\Attributes\DY)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterDY,CurrentObject\Attributes\DY,tex$)

	Case "DZ"
		tex$=Str$(CurrentObject\Attributes\DZ)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterDZ,CurrentObject\Attributes\DZ,tex$)

	Case "MoveXGoal"
		tex$=Str$(CurrentObject\Attributes\MoveXGoal)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterMoveXGoal,CurrentObject\Attributes\MoveXGoal,tex$)

	Case "MoveYGoal"
		tex$=Str$(CurrentObject\Attributes\MoveYGoal)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterMoveYGoal,CurrentObject\Attributes\MoveYGoal,tex$)

	Case "Data10"
		tex$=Str$(CurrentObject\Attributes\Data10)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterData10,CurrentObject\Attributes\Data10,tex$)

	Case "Caged"
		tex$=Str$(CurrentObject\Attributes\Caged)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterCaged,CurrentObject\Attributes\Caged,tex$)

	Case "Dead"
		tex$=Str$(CurrentObject\Attributes\Dead)
		Select CurrentObject\Attributes\Dead
			Case 1
				tex$="Spinning"
			Case 3
				tex$="Sinking"
		End Select
		tex$=SetAdjusterDisplayInt(ObjectAdjusterDead,CurrentObject\Attributes\Dead,tex$)

	Case "DeadTimer"
		tex$=Str$(CurrentObject\Attributes\DeadTimer)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterDeadTimer,CurrentObject\Attributes\DeadTimer,tex$)

	Case "MovementTimer"
		tex$=Str$(CurrentObject\Attributes\MovementTimer)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterMovementTimer,CurrentObject\Attributes\MovementTimer,tex$)

	Case "Flying"
		State$="Grounded"
		If CurrentObject\Attributes\Flying/10 = 1	; bounced by spring
			If CurrentObject\Attributes\Flying Mod 10 >=1 And CurrentObject\Attributes\Flying Mod 10<=3 Then State$="Spr East"
			If CurrentObject\Attributes\Flying Mod 10 >=5 And CurrentObject\Attributes\Flying Mod 10<=7 Then State$="Spr West"
			If CurrentObject\Attributes\Flying Mod 10 >=3 And CurrentObject\Attributes\Flying Mod 10<=5 Then State$="Spr South"
			If CurrentObject\Attributes\Flying Mod 10 >=7 Or CurrentObject\Attributes\Flying Mod 10<=1 Then State$="Spr North"
		EndIf

		If CurrentObject\Attributes\Flying/10 = 2	; on ice
			If CurrentObject\Attributes\Flying Mod 10 >=1 And CurrentObject\Attributes\Flying Mod 10<=3 Then State$="Ice East"
			If CurrentObject\Attributes\Flying Mod 10 >=5 And CurrentObject\Attributes\Flying Mod 10<=7 Then State$="Ice West"
			If CurrentObject\Attributes\Flying Mod 10 >=3 And CurrentObject\Attributes\Flying Mod 10<=5 Then State$="Ice South"
			If CurrentObject\Attributes\Flying Mod 10 >=7 Or CurrentObject\Attributes\Flying Mod 10<=1 Then State$="Ice North"
		EndIf

		tex$=CurrentObject\Attributes\Flying+" ("+State+")"
		tex$=SetAdjusterDisplayInt(ObjectAdjusterFlying,CurrentObject\Attributes\Flying,tex$)

	Case "Indigo"
		tex$=Str$(CurrentObject\Attributes\Indigo)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterIndigo,CurrentObject\Attributes\Indigo,tex$)

	Case "Speed"
		tex$=Str$(CurrentObject\Attributes\Speed)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterSpeed,CurrentObject\Attributes\Speed,tex$)

	Case "Radius"
		tex$=Str$(CurrentObject\Attributes\Radius)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterRadius,CurrentObject\Attributes\Radius,tex$)

	Case "Status"
		tex$=Str$(CurrentObject\Attributes\Status)

		If CurrentObject\Attributes\LogicType=50 ; spellball
			tex2$="FromPlayer"
			If CurrentObject\Attributes\Status=0 tex$="No"
			If CurrentObject\Attributes\Status=1 tex$="Yes"
		EndIf

		If CurrentObject\Attributes\LogicType=434 ; Mothership
			If CurrentObject\Attributes\Status=0
				tex$="Goal 1"
			ElseIf CurrentObject\Attributes\Status=1
				tex$="Goal 2"
			ElseIf CurrentObject\Attributes\Status=2
				tex$="Goal 3"
			ElseIf CurrentObject\Attributes\Status<-199
				tex$=CurrentObject\Attributes\Status+"/Eversplode"
			ElseIf CurrentObject\Attributes\Status<0
				tex$="Exploding "+Str$(-CurrentObject\Attributes\Status)
			EndIf
		EndIf

		If CurrentObject\Attributes\LogicType=470 Or CurrentObject\Attributes\LogicType=471 ; Wraith or Ghost
			Select CurrentObject\Attributes\Status
			Case 0
				tex$="Hidden"
			Case 1
				tex$="Attacking"
			Case 2
				tex$="Watching"
			End Select
		EndIf

		tex$=SetAdjusterDisplayInt(ObjectAdjusterStatus,CurrentObject\Attributes\Status,tex$)

	End Select

	If HighlightWopAdjusters And AdjusterAppearsInWop(ObjectAdjuster$(i))
		Color TextAdjusterHighlightedR,TextAdjusterHighlightedG,TextAdjusterHighlightedB
	Else
		Color TextAdjusterR,TextAdjusterG,TextAdjusterB
	EndIf

	CrossedOut=CurrentAdjusterRandomized

	If CrossedOut
		tex$=tex2$
		Dashes$=""
		For t=1 To Len(tex2$)
			Dashes$=Dashes$+"-"
		Next

		HalfNameWidth=4*Len(tex$)

		Text StartX+92-HalfNameWidth,StartY,Dashes$

		Text StartX+2,StartY,LeftAdj$
		Text StartX+182-8*Len(RightAdj$),StartY,RightAdj$

	ElseIf tex2$<>"" And ObjectAdjuster$(i)<>"TextData0" And ObjectAdjuster$(i)<>"TextData1"
		If CurrentAdjusterAbsolute
			tex$=tex2$+": "+tex$
		Else
			If CurrentAdjusterZero
				tex$=tex2$+": ..."
			Else
				tex$=tex2$+" += "+tex$
			EndIf
		EndIf
	EndIf

	Text StartX+92-4*Len(tex$),StartY,tex$

End Function