Function GetCommandName$(id)
	Select id
	Case 1
		Return "Activate"
	Case 2
		Return "Deactivate"
	Case 3
		Return "Toggle"
	Case 4
		Return "Change Int"
	Case 5
		Return "Destroy"
	Case 6
		Return "Change Light"
	Case 7
		Return "Warp Lvl"
	Case 8
		Return "Start Adv"
	Case 9
		Return "Earthquake"
	Case 10
		Return "Global SFX"
	Case 11
		Return "Local SFX"
	Case 12
		Return "Adjust Music"
	Case 13
		Return "Change Weather"
	Case 14
		Return "(MOD) Set Music"
	Case 15
		Return "(MOD) Set Magic"
	Case 16
		Return "(MOD) Set Cam"
	Case 17
		Return "(MOD) TileLogic"
	Case 18
		Return "(MOD) Set OTL"
	Case 21
		Return "Start Dialog"
	Case 22
		Return "Chng Dia Start"
	Case 23
		Return "Acti. AskAbout"
	Case 24
		Return "Deac. AskAbout"
	Case 25
		Return "Togg. AskAbout"
	Case 26
		Return "Set AA Active"
	Case 27
		Return "Set AskAbt Dia"
	Case 28
		Return "Acti. MstrAA"
	Case 29
		Return "Deac. MstrAA"
	Case 30
		Return "Togg. MstrAA"
	Case 41
		Return "Silent CopyObj"
	Case 42
		Return "Flashy CopyObj"
	Case 51
		Return "Chng MvmtType"
	Case 52
		Return "Chng MvTp+Data"
	Case 61
		Return "Move NPC"
	Case 62
		Return "Change NPC 1"
	Case 63
		Return "Change NPC 2"
	Case 64
		Return "Exclamation"
	Case 65
		Return "(MOD) Plyr Face"
	Case 101
		Return "(MOD) Cutscene"
	Case 102
		Return "Cutscene 1"
	Case 103
		Return "Cutscene 2"
	Case 104
		Return "Cutscene 3"
	Case 111
		Return "Refill Lamp"
	Case 112
		Return "4^2 Inventory"
	Case 113
		Return "5^2 Inventory"
	Case 114
		Return "Enable Shards"
	Case 115
		Return "Floing Menu"
	Case 116
		Return "Change Hat"
	Case 117
		Return "Chng Accessory"
	Case 118
		Return "(MOD) Hub End"
	Case 119
		Return "(MOD) Dischrge"
	Case 120
		Return "(MOD) POTZ End"
	Default
		Return "N/A"
	End Select
End Function

Function GetCMDData1Name$(id)
	Select id
	Case 1,2,3,4,5,16,51,52,64
		Return "Target ID"
	Case 6
		Return "Red"
	Case 7,120
		Return "Level"
	Case 8
		Return "Adv. No"
	Case 9
		Return "Duration"
	Case 10,11
		Return "Sound"
	Case 12
		Return "TargetVolume"
	Case 13
		Return "Weather"
	Case 14
		Return "Music ID"
	Case 15
		Return "Magic"
	Case 17,18
		Return "Tile X"
	Case 21,22,23,24,25,26,27
		Return "Dialog No"
	Case 28,29,30
		Return "MstrAA"
	Case 41,42
		Return "Source X"
	Case 61,62,63
		Return "NPC ID"
	Case 65
		Return "Expression"
	Case 101
		Return "Cutscene ID"
	Default
		Return "N/A (1)"
	End Select
End Function

Function GetCMDData2Name$(id)
	Select id
	Case 4
		Return "Int"
	Case 6
		Return "Green"
	Case 7
		Return "Start X"
	Case 11
		Return "Source X"
	Case 12
		Return "MaxVolumeStep" ;"Volume Step"
	Case 15
		Return "Charges"
	Case 17,18
		Return "Tile Y"
	Case 21,22
		Return "Interchange"
	Case 23,24,25,26,27
		Return "AskAbout"
	Case 41,42
		Return "Source Y"
	Case 51
		Return "Obsolete"
	Case 61,120
		Return "Dest X"
	Case 52
		Return "MvmtType"
	Case 62
		Return "Dialog"
	Case 63
		Return "WalkAnim"
	Case 64
		Return "Particle ID"
	Default
		Return "N/A (2)"
	End Select
End Function

Function GetCMDData3Name$(id)
	Select id
	Case 4,26
		Return "Value"
	Case 6
		Return "Blue"
	Case 7
		Return "Start Y"
	Case 11
		Return "Source Y"
	Case 12
		Return "TargetPitch"
	Case 15
		Return "Homing"
	Case 17
		Return "Logic"
	Case 18
		Return "ObjectTileLogic"
	Case 21
		Return "Obsolete?"
	Case 27
		Return "Interchange"
	Case 41,42
		Return "Dest X"
	Case 51
		Return "Obsolete"
	Case 61,120
		Return "Dest Y"
	Case 52
		Return "MvTpData"
	Case 62
		Return "Expression"
	Case 63
		Return "Turn"
	Case 64
		Return "Count"
	Default
		Return "N/A (3)"
	End Select
End Function

Function GetCMDData4Name$(id)
	Select id
	Case 4
		Return "Level?"
	Case 7
		Return "PlayerYaw"
	Case 12
		Return "MaxPitchStep"
	Case 41,42
		Return "Dest Y"
	Case 51
		Return "MvmtType"
	Case 62
		Return "Yaw"
	Case 63
		Return "IdleAnim"
	Default
		Return "N/A (4)"
	End Select
End Function

Function GetCmdData1ValueName$(Cmd, Data1)

	Select Cmd
	Case 6
		Return GetLightColorAndAmbientString$(Data1)
	Case 10,11
		Return Data1+"/"+GetSoundName$(Data1)
	Case 12
		If Data1=0
			Return "No Change"
		Else
			Return Data1+"%"
		EndIf
	Case 13
		Return GetWeatherName$(Data1)
	Case 14
		Return GetMusicName$(Data1)
	Case 15
		Return GetMagicNameAndId$(Data1)
	Case 16,64
		If Data1=-1
			Return "Pla"
		Else
			Return Data1
		EndIf
	Case 65
		Return GetStinkerExpressionName$(Data1)
	Default
		Return Data1
	End Select

End Function

Function GetCmdData2ValueName$(Cmd, Data2)

	Select Cmd
	Case 4
		Return Data2+"/"+GetCmd4Data2ValueName$(Data2)
	Case 6
		Return GetLightColorAndAmbientString$(Data2)
	Case 12
		If Data2=0
			Return "Instant"
		Else
			Return Data2+"%"
		EndIf
	Case 21
		If Data2=-1
			Return "Current"
		Else
			Return Data2
		EndIf
	Case 52
		Return Data2+"/"+GetMovementTypeString$(Data2)
	Case 62
		If Data2=0
			Return "None"
		ElseIf Data2=-1
			Return "No Change"
		Else
			Return Data2
		EndIf
	Case 63
		If Data2=-1
			Return "No Change"
		Else
			Return GetStinkerNPCWalkAnimName$(Data2)
		EndIf
	Default
		Return Data2
	End Select

End Function

Function GetCmdData3ValueName$(Cmd, Data2, Data3)

	Select Cmd
	Case 4
		Select Data2
		Case 1 ; MovementType
			Return Data3+"/"+GetMovementTypeString$(Data3)
		Case 9 ; Type
			Return Data3+"/"+GetTypeString$(Data3)
		Case 12 ; ActivationType
			Return GetActivationTypeString$(Data3)
		Default
			Return Data3
		End Select
	Case 6
		Return GetLightColorAndAmbientString$(Data3)
	Case 12
		If Data3=0
			Return "No Change"
		Else
			Return Data3+" kHz"
		EndIf
	Case 15
		Return Data3+"/"+OneToYes$(Data3)
	Case 17
		Return LogicIdToLogicName$(Data3)
	Case 26
		Return GetAskAboutActiveNameShort$(Data3)
	Case 62
		If Data3=-1
			Return "No Change"
		Else
			Return GetStinkerExpressionName$(Data3)
		EndIf
	Case 63
		If Data3=-1
			Return "No Change"
		Else
			Return GetNPCTurningName$(Data3)
		EndIf
	Default
		Return Data3
	End Select

End Function

Function GetCmdData4ValueName$(Cmd, Data4)

	Select Cmd
	Case 4
		If Data4=0
			Return "Current"
		Else
			Return Data4+"/MAV?"
		EndIf
	Case 7
		DisplayedRotation=(Data4+180) Mod 360
		Return GetDirectionString$(DisplayedRotation)
	Case 12
		If Data4=0
			Return "Instant"
		Else
			Return Data4+" kHz"
		EndIf
	Case 51
		Return Data4+"/"+GetMovementTypeString$(Data4)
	Case 62
		If Data4=-1
			Return "No Change"
		Else
			Return Data4
			;DisplayedRotation=Data4 Mod 360
			;Return GetDirectionString$(DisplayedRotation)
		EndIf
	Case 63
		If Data4=-1
			Return "No Change"
		Else
			Return GetStinkerNPCIdleAnimName$(Data4)
		EndIf
	Default
		Return Data4
	End Select

End Function

Function GetCmd4Data2ValueName$(value)

	Select value
	Case 1
		Return "MovementType"
	Case 2
		Return "MovementTypeData"
	Case 3
		Return "RadiusType"
	Case 4
		Return "Data10"
	Case 5
		Return "AttackPower"
	Case 6
		Return "DefensePower"
	Case 7
		Return "DestructionType"
	Case 8
		Return "ID"
	Case 9
		Return "Type"
	Case 10
		Return "SubType"
	Case 11
		Return "Active"
	Case 12
		Return "ActivationType"
	Case 13
		Return "ActivationSpeed"
	Case 14
		Return "Status"
	Case 15
		Return "Timer"
	Case 16
		Return "TimerMax1"
	Case 17
		Return "TimerMax2"
	Case 18
		Return "Teleportable"
	Case 19
		Return "ButtonPush"
	Case 20
		Return "WaterReact"
	Case 21
		Return "Telekinesisable"
	Case 22
		Return "Freezable"
	Case 23
		Return "Data0"
	Case 24
		Return "Data1"
	Case 25
		Return "Data2"
	Case 26
		Return "Data3"
	Case 27
		Return "Data4"
	Case 28
		Return "Data5"
	Case 29
		Return "Data6"
	Case 30
		Return "Data7"
	Case 31
		Return "Data8"
	Case 32
		Return "Data9"
	Default
		Return "None"
	End Select

End Function

Function GetLightColorAndAmbientString$(ColorValue)

	Return ColorValue+" (Amb: "+ColorValue/2+")"

End Function

Function GetMovementTypeString$(value)

	Select value
	Case 0
		Return "Stationary"
	Case 10
		Return "HPathfind0" ;"HPathfind0" "GoalRange0" "HQ A* To 0" ""A*+ Range0""
	Case 11
		Return "HPathfind1"
	Case 12
		Return "HPathfind2"
	Case 13
		Return "MPathfind0"
	Case 14
		Return "MPathfind1"
	Case 15
		Return "MPathfind2"
	Case 16
		Return "LPathfind0"
	Case 17
		Return "LPathfind1"
	Case 18
		Return "LPathfind2"
	Case 20
		Return "PlayerPath"
	Case 30
		Return "FleeRange0"
	Case 31
		Return "FleeRange1"
	Case 32
		Return "FleeRange2"
	Case 33
		Return "FleeRange3"
	Case 34
		Return "FleeRange4"
	Case 41
		Return "NorthLeft"
	Case 42
		Return "NorthRight"
	Case 43
		Return "EastLeft"
	Case 44
		Return "EastRight"
	Case 45
		Return "SouthLeft"
	Case 46
		Return "SouthRight"
	Case 47
		Return "WestLeft"
	Case 48
		Return "WestRight"
	Case 51,52
		Return "NorthSwim"
	Case 53,54
		Return "EastSwim"
	Case 55,56
		Return "SouthSwim"
	Case 57,58
		Return "WestSwim"
	Case 71
		Return "NorthBounc"
	Case 72
		Return "NorthEast"
	Case 73
		Return "EastBounc"
	Case 74
		Return "SouthEast"
	Case 75
		Return "SouthBounc"
	Case 76
		Return "SouthWest"
	Case 77
		Return "WestBounc"
	Case 78
		Return "NorthWest"
	Case 81
		Return "MoobotNL"
	Case 82
		Return "MoobotNR"
	Case 83
		Return "MoobotEL"
	Case 84
		Return "MoobotER"
	Case 85
		Return "MoobotSL"
	Case 86
		Return "MoobotSR"
	Case 87
		Return "MoobotWL"
	Case 88
		Return "MoobotWR"
	Default
		Return "NotVanilla"
	End Select

End Function

Function GetActivationTypeString$(value)

	Select value
	Case 1
		Return "GrowZ"
	Case 2
		Return "GrowXYZ"
	Case 3
		Return "GrowXY"
	Case 11
		Return "GoDown"
	Case 12
		Return "Bridge1"
	Case 13
		Return "Bridge2"
	Case 14
		Return "Bridge3"
	Case 15
		Return "Bridge4"
	Case 16
		Return "Bridge5"
	Case 17
		Return "GoNorth"
	Case 18
		Return "GoEast"
	Case 19
		Return "GoSouth"
	Case 20
		Return "GoWest"
	Case 21
		Return "Fade"
	Case 31
		Return "Cage"
	Case 41
		Return "DungeonDoor"
	Default
		Return value
	End Select

End Function

Function GetMusicName$(value)

	Select value
	Case -1
		Return "Beach"
	Case 0
		Return "No Music"
	Case 1
		Return "WA Intro"
	Case 2
		Return "Pastoral"
	Case 3
		Return "WonderTown"
	Case 4
		Return "Dark/Sewer"
	Case 5
		Return "Cave/Woods"
	Case 6
		Return "Scary/Void"
	Case 7
		Return "WondrFalls"
	Case 8
		Return "Jungle"
	Case 9
		Return "KaboomTown"
	Case 10
		Return "Acid Pools"
	Case 11
		Return "Retro"
	Case 12
		Return "Cave"
	Case 13
		Return "POTZ Intro"
	Case 14
		Return "Uo Sound"
	Case 15
		Return "Z-Ambience"
	Case 16
		Return "Z-Synchron"
	Case 17
		Return "RetroScary"
	Case 18
		Return "DesertWind"
	Case 19
		Return "DesertCave"
	Case 20
		Return "Star World"
	Case 21
		Return "Piano"
	Default
		Return value
	End Select

End Function

Function GetWeatherName$(value)

	Select value
	Case 0
		Return "Clear Sky"
	Case 1
		Return "Light Snow"
	Case 2
		Return "Heavy Snow"
	Case 3
		Return "BlizzardRL"
	Case 4
		Return "BlizzardLR"
	Case 5
		Return "Rain"
	Case 6
		Return "Weird"
	Case 7
		Return "ThundrStrm"
	Case 8
		Return "Alarm"
	Case 9
		Return "Light Rise"
	Case 10
		Return "Light Fall"
	Case 11
		Return "Rainb Rise"
	Case 12
		Return "Rainb Fall"
	Case 13
		Return "Foggy"
	Case 14
		Return "FoggyGreen"
	Case 15
		Return "Leaves"
	Case 16
		Return "Sand Storm"
	Case 17
		Return "Abstract"
	Default
		Return "NotVanilla"
	End Select

End Function

Function GetSoundName$(value)

	Select value
	Case 0
		Return "GetStar"
	Case 1
		Return "TollGate"
	Case 10
		Return "SpringHit"
	Case 11
		Return "GetGem"
	Case 12
		Return "GetCoin"
	Case 13
		Return "CrystalTone"
	Case 14
		Return "Wakka"
	Case 15
		Return "Destroy"
	Case 16
		Return "ElectricZap"
	Case 20
		Return "IceSlide"
	Case 21
		Return "ButtonDown"
	Case 22
		Return "Rotator"
	Case 23
		Return "ButtonTimer"
	Case 24
		Return "ColorChange"
	Case 28
		Return "Ghost"
	Case 29
		Return "WraithAppear"
	Case 30
		Return "FireTrapOn"
	Case 31
		Return "FireTrapLoop"
	Case 32
		Return "CageFall"
	Case 33
		Return "BrdgWaterUp"
	Case 34
		Return "BrdgWaterDown"
	Case 35
		Return "BrdgMechaUp"
	Case 36
		Return "BrdgMechaDown"
	Case 37
		Return "DungeonDoor"
	Case 38
		Return "AutoDoorOpen"
	Case 39
		Return "AutoDoorClose"
	Case 40
		Return "TransportLoop"
	Case 41
		Return "TransportStop"
	Case 42
		Return "TeleporterUse"
	Case 43
		Return "SucTubeUse2"
	Case 44
		Return "SucTubeUse1"
	Case 45
		Return "PlayerOof"
	Case 50
		Return "WeeHiThere"
	Case 51
		Return "WeeHi"
	Case 52
		Return "WeeMorning"
	Case 53
		Return "WeeHello"
	Case 54
		Return "WeeYooHoo"
	Case 55
		Return "WeeYeah?"
	Case 56
		Return "WeeHmm?"
	Case 57
		Return "WeeWhat?"
	Case 58
		Return "WeeUhHuh?"
	Case 59
		Return "WeeSnore"
	Case 60
		Return "WeeOkay"
	Case 61
		Return "WeeSndsGood"
	Case 62
		Return "WeeOkeeDokee"
	Case 63
		Return "WeeHereIGo"
	Case 64
		Return "WeeYee"
	Case 65
		Return "WeeDeath"
	Case 66
		Return "WeeOhNo"
	Case 67
		Return "WeeDrown"
	Case 68
		Return "WeeImBored"
	Case 69
		Return "WeeImTired"
	Case 70
		Return "WeeWoo"
	Case 71
		Return "WeeThankYou"
	Case 72
		Return "WeeByeBye"
	Case 73
		Return "WeeYay"
	Case 74
		Return "BoomerKaboom?"
	Case 75
		Return "BoomerKaboom1"
	Case 76
		Return "BoomerKaboom2"
	Case 77
		Return "BoomerKa"
	Case 78
		Return "BoomerKaboom!"
	Case 79
		Return "BoomerScared"
	Case 80
		Return "MagicCharge"
	Case 81
		Return "Blinked"
	Case 82
		Return "CastSpell"
	Case 83
		Return "MakeBrrFloat"
	Case 84
		Return "IceShatter"
	Case 85
		Return "StinkerFrozen"
	Case 86
		Return "ChomperFrozen"
	Case 87
		Return "ThwartFrozen"
	Case 88
		Return "SpellIceWall"
	Case 90
		Return "TeleporterOn"
	Case 91
		Return "TeleporterOff"
	Case 92
		Return "NewGrowFlower"
	Case 93
		Return "FloingBubble"
	Case 95
		Return "MothrshipLoop"
	Case 96
		Return "MothrshipDie"
	Case 97
		Return "LurkerChomp"
	Case 98
		Return "MoobotMove"
	Case 99
		Return "MoobotStop"
	Case 100
		Return "ScritterMove"
	Case 101
		Return "ChomperNyak"
	Case 102
		Return "FireFlwrOn"
	Case 103
		Return "FireFlwrSpit"
	Case 104
		Return "FireFlwrHurt"
	Case 105
		Return "FireFlwrDie"
	Case 106
		Return "ThwartStep"
	Case 107
		Return "ThwartLaugh"
	Case 108
		Return "TurtleGetWet"
	Case 109
		Return "SpikeyBaLoop"
	Case 110
		Return "CuboidBreak"
	Case 111
		Return "TentacleUp"
	Case 112
		Return "TentacleDown"
	Case 113
		Return "IceTrllGrunt"
	Case 114
		Return "IceTrllFroze"
	Case 115
		Return "CrabMove"
	Case 116
		Return "CrabAwaken"
	Case 117
		Return "CrabPowed"
	Case 118
		Return "CoilyBounce"
	Case 119
		Return "MechaNyak"
	Case 120
		Return "Waterfall1"
	Case 121
		Return "DuckQuack"
	Case 122
		Return "Earthquake"
	Case 123
		Return "VoidLoop"
	Case 124
		Return "WaterDroplet"
	Case 125
		Return "Waterfall2"
	Case 126
		Return "Ocean1"
	Case 127
		Return "Ocean2"
	Case 128
		Return "Seagulls1"
	Case 129
		Return "Seagulls2"
	Case 130
		Return "MenuSelect"
	Case 131
		Return "DialogOpen"
	Case 132
		Return "DialogClose"
	Case 133
		Return "VoLoadGame"
	Case 134
		Return "VoSaveGame"
	Case 135
		Return "VoAreUSure?"
	Case 136
		Return "ReplySelect"
	Case 137
		Return "VoPleaseWait"
	Case 138
		Return "DeepWind"
	Case 139
		Return "Harp"
	Case 140
		Return "ZBotElimin8"
	Case 141
		Return "ZBotIAmAZBot"
	Case 142
		Return "ZBotWeAreThe"
	Case 143
		Return "ZBotIntruder"
	Case 144
		Return "ZBotIAmError"
	Case 145
		Return "ZBotNoCmpute"
	Case 146
		Return "ZBotEndOLine"
	Case 147
		Return "ZBotChicken"
	Case 148
		Return "ZBotYourBase"
	Case 149
		Return "ZBotFutile"
	Case 150
		Return "StinkerAAHHH"
	Case 151
		Return "StinkerOw"
	Case 152
		Return "StinkerDrown"
	Case 153
		Return "GettingHot"
	Case 154
		Return "OwHotHotHot"
	Case 155
		Return "Thunder1"
	Case 156
		Return "Thunder2"
	Case 157
		Return "Thunder3"
	Case 158
		Return "ZBotStnkrDie"
	Case 159
		Return "ZBotElimTheS"
	Case 160
		Return "PlayerLose1"
	Case 161
		Return "PlayerLose2"
	Case 162
		Return "PlayerLose3"
	Case 163
		Return "PlayerLose4"
	Case 164
		Return "PlayerStart1"
	Case 165
		Return "PlayerStart2"
	Case 166
		Return "PlayerStart3"
	Case 167
		Return "PlayerStart4"
	Case 168
		Return "PlayerStart5"
	Case 169
		Return "PlayerGreet"
	Case 170
		Return "PlayerSlide1"
	Case 171
		Return "PlayerSlide2"
	Case 172
		Return "PlayerSlide3"
	Case 173
		Return "GetCustItem"
	Case 174
		Return "GetCustItem"
	Case 175
		Return "PlayerAww"
	Case 176
		Return "PlayerUse"
	Case 177
		Return "NPCNice2CU"
	Case 180
		Return "StnkrIceYoof"
	Case 181
		Return "StnkrIceWoo1"
	Case 182
		Return "StnkrIceWoo2"
	Case 187
		Return "NPCHiWatUDng"
	Case 188
		Return "NPCItNice2CU"
	Case 189
		Return "NPCHowRThee"
	Case 190
		Return "NPCHello!"
	Case 191
		Return "NPCHiHowRYa"
	Case 192
		Return "NPCYooloo"
	Case 193
		Return "NPCHloNic2CU"
	Case 194
		Return "NPCHelloFem"
	Case 195
		Return "NPCWatsCookn"
	Case 196
		Return "NPCHello"
	Case 197
		Return "NPCYup"
	Case 198
		Return "NPCWhatUDoin"
	Case 199
		Return "NPCNice2CU!"
	Default
		Return "N/A"
	End Select

End Function

Function GetStinkerExpressionName$(Value)

	Select Value
	Case 0
		Return "Happy"
	Case 1
		Return "Surprised"
	Case 2
		Return "Sad"
	Case 3
		Return "Asleep"
	Case 4
		Return "Angry"
	Default
		Return Value+"/Unknown"
	End Select

End Function

Function GetStinkerNPCWalkAnimName$(Value)

	Select Value
	Case -1
		Return "All"
	Case 0
		Return "Waddle"
	Case 1
		Return "Walk"
	Case 2
		Return "Run"
	Case 3
		Return "CastSpell1"
	Case 4
		Return "CastSpell2"
	Case 5
		Return "Reach"
	Case 6
		Return "CastSpell4"
	Case 7
		Return "WaveAndDrop"
	Case 8
		Return "FootTap"
	Case 9
		Return "Sway"
	Case 10
		Return "Panic1"
	Case 11
		Return "Dance"
	Case 12
		Return "Sit1"
	Case 13
		Return "Sit2"
	Case 14
		Return "WaveFast"
	Case 15
		Return "PraiseTheSun"
	Case 16
		Return "Sit3"
	Case 17
		Return "SitVibrate"
	Case 18
		Return "Panic2"
	Case 19
		Return "WaveNormal"
	Case 20
		Return "Sit5"
	Case 21
		Return "Sit6"
	Default
		Return Value+"/None"
	End Select

End Function

Function GetNPCTurningName$(Value)

	tex$=Value+"/Unknown"
	If (Value Mod 10)=0 tex$="Fixed"
	If (Value Mod 10)=1 tex$="Player"
	If (Value Mod 10)=2 tex$="Clock Slow"
	If (Value Mod 10)=3 tex$="Clock Fast"
	If (Value Mod 10)=4 tex$="Count Slow"
	If (Value Mod 10)=5 tex$="Count Fast"
	If Value>=10 And Value<20 tex$=tex$+"Bounce"
	If Value>=20 And Value<30 tex$=tex$+"BouFas"
	If Value>=30 tex$=tex$+"SpdAnm"
	Return tex$

End Function

Function GetStinkerNPCIdleAnimName$(Value)

	Select Value
	Case 0
		Return "Sway"
	Case 1
		Return "WaveSometime"
	Case 2
		Return "WaveConstant"
	Case 3
		Return "FootSometime"
	Case 4
		Return "FootConstant"
	Case 5
		Return "Dance"
	Case 6
		Return "SitConstant"
	Case 7
		Return "Sit/Stand"
	Case 8
		Return "Sit/StandWave"
	Case 9
		Return "PanicSometime"
	Case 10
		Return "PanicConstant"
	Default
		Return Value+"/NotVanilla"
	End Select

End Function

Function GetItemFnName$(value)

	Select value
	Case 1
		Return "OpenRucksack"
	Case 2
		Return "CloseRucksack"
	Case 3
		Return "Objective"
	Case 4
		Return "Menu"
	Case 1001
		Return "Gloves"
	Case 1002,1003,1004,1005,1006,1007,1008,1009,1010
		Return "GloveIcon"+(value-1001)
	Case 2001
		Return "Lamp"
	Case 2002
		Return "LampActive"
	Case 2011
		Return "GlowGem"
	Case 2012
		Return "GlowGemActive"
	Case 2021
		Return "Spy-Eye"
	Case 2022
		Return "Spy-EyeActive"
	Case 2031
		Return "Token"
	Case 2041
		Return "Glyph"
	Case 2051
		Return "MapPiece"
	Case 3001
		Return "Key"
	Case 3011
		Return "Whistle"
	Case 3021
		Return "Shard"
	Case 3091
		Return "WonAdventure"
	Case 4001
		Return "None"
	Case 4101
		Return "None *"
	Case 4201
		Return "None +"
	Case 4011
		Return "Win Adventure"
	Case 4111
		Return "Win Adventure *"
	Case 4211
		Return "Win Adventure +"
	Case 4021
		Return "ID On Local"
	Case 4121
		Return "ID On Local *"
	Case 4221
		Return "ID On Local +"
	Case 4031
		Return "ID Off Local"
	Case 4131
		Return "ID Off Local *"
	Case 4231
		Return "ID Off Local +"
	Case 4041
		Return "ID Tog Local"
	Case 4141
		Return "ID Tog Local *"
	Case 4241
		Return "ID Tog Local +"
	Case 4051
		Return "ID On Global"
	Case 4151
		Return "ID On Global *"
	Case 4251
		Return "ID On Global +"
	Case 4061
		Return "ID Off Global"
	Case 4161
		Return "ID Off Global *"
	Case 4261
		Return "ID Off Global +"
	Case 4071
		Return "ID Tog Global"
	Case 4171
		Return "ID Tog Global *"
	Case 4271
		Return "ID Tog Global +"
	Case 9091
		Return "Empty"
	Default
		Return value
	End Select

End Function

Function GetFlipBridgeDirectionName$(Data2)

	Modded=EuclideanRemainderInt(Data2,4)
	Select Modded
	Case 0
		TheString$=Data2+". N/S"
	Case 1
		TheString$=Data2+". NE/SW"
	Case 2
		TheString$=Data2+". E/W"
	Case 3
		TheString$=Data2+". SE/NW"
	Default
		TheString$=Modded
	End Select
	Return TheString$

End Function

Function GetCMDData1NameAndValue$(Cmd,Data1,Joiner$)

	Return GetCmdData1Name$(Cmd)+Joiner$+GetCmdData1ValueName$(Cmd,Data1)

End Function

Function GetCMDData2NameAndValue$(Cmd,Data2,Joiner$)

	Return GetCmdData2Name$(Cmd)+Joiner$+GetCmdData2ValueName$(Cmd,Data2)

End Function

Function GetCMDData3NameAndValue$(Cmd,Data2,Data3,Joiner$)

	Return GetCmdData3Name$(Cmd)+Joiner$+GetCmdData3ValueName$(Cmd,Data2,Data3)

End Function

Function GetCMDData4NameAndValue$(Cmd,Data4,Joiner$)

	Return GetCmdData4Name$(Cmd)+Joiner$+GetCmdData4ValueName$(Cmd,Data4)

End Function

Function GetCmdData1ExtraInfo$(Cmd,Data1)

	Select Cmd
	Case 8
		Return GetAdventureName$(Data1)
	Case 21,22,23,24,25,26,27
		Return PreviewDialog$(Data1,0)
	End Select

	Return ""

End Function

Function GetCmdData2ExtraInfo$(Cmd,Data1,Data2)

	Select Cmd
	Case 21,22
		If Data2>-1
			Return PreviewDialog$(Data1,Data2)
		EndIf
	Case 23,24,25,26,27
		Return PreviewAskAbout$(Data1,Data2)
	Case 64
		Return "{PARTICLE}"+Data2
	End Select

	Return ""

End Function

Function GetCmdData3ExtraInfo$(Cmd,Data1,Data3)

	Select Cmd
	Case 27
		Return PreviewDialog$(Data1,Data3)
	End Select

	Return ""

End Function

Function WithJoinerIfNotEmpty$(TheString$,Joiner$)

	If TheString$=""
		Return TheString$
	Else
		Return Joiner$+TheString$
	EndIf

End Function

Function GetTypeString$(value)

	Select value
	Case 0
		Return "None"
	Case 1
		Return "Player"
	Case 10
		Return "Gate"
	Case 11
		Return "Tollgate"
	Case 20
		Return "Fire Trap"
	Case 30
		Return "Teleporter"
	Case 40
		Return "SteppingStone"
	Case 45
		Return "Conveyor Lead"
	Case 46
		Return "Conveyor Tail"
	Case 50
		Return "Spellball"
	Case 51
		Return "Magic Shooter"
	Case 52
		Return "Meteor Shooter"
	Case 53
		Return "Meteorite"
	Case 54
		Return "Magic Mirror"
	Case 60
		Return "IceCub/FlngBub"
	Case 70
		Return "BetaPickupItem"
	Case 71
		Return "BetaUsedItem"
	Case 80,81
		Return "WallblockNever"
	Case 82,83
		Return "KeyblockNever"
	Case 84
		Return "KeyblockSpam"
	Case 85
		Return "WallblockOnce"
	Case 86
		Return "WallblockSpam"
	Case 87
		Return "KeyblockOnce"
	Case 90
		Return "Button"
	Case 100
		Return "StinkerHatAcc"
	Case 101
		Return "Shadow"
	Case 110;,111,112,113,114,115,116,117,118,119
		Return "Stinker NPC"
	Case 120
		Return "Wee Stinker"
	Case 130
		Return "Stinker Exit"
	Case 140
		Return "Cage"
	Case 150
		Return "Scritter"
	Case 151
		Return "RainbowBubble"
	Case 160
		Return "Scenery"
	Case 161
		Return "Waterfall"
	Case 162
		Return "Cottage"
	Case 163
		Return "WindmillRotor"
	Case 164
		Return "Fountain"
	Case 165
		Return "ArcadeMachine"
	Case 166
		Return "SkyMachineMap"
	Case 170
		Return "Gold Star"
	Case 171
		Return "Coin"
	Case 172
		Return "Key/Keycard"
	Case 173
		Return "Gem"
	Case 174
		Return "Token"
	Case 179
		Return "Custom Item"
	Case 180
		Return "Sign"
	Case 190
		Return "ParticleSpawn"
	Case 200
		Return "Magic Charger"
	Case 210
		Return "Transporter"
	Case 220
		Return "Turtle"
	Case 230
		Return "FireFlower"
	Case 240
		Return "Barrel Reg"
	Case 241
		Return "Barrel TNT"
	Case 242
		Return "Cuboid"
	Case 250
		Return "Chomper"
	Case 260
		Return "Spikeyball"
	Case 270
		Return "Btrfly/Glworm"
	Case 271
		Return "Zipper"
	Case 280
		Return "Spring"
	Case 281
		Return "Suctube -"
	Case 282
		Return "Suctube X"
	Case 290
		Return "Thwart"
	Case 300
		Return "Brr Float"
	Case 301
		Return "RainbowFloat"
	Case 310
		Return "RubberDucky"
	Case 320
		Return "Void"
	Case 330
		Return "Wysp"
	Case 340
		Return "Tentacle"
	Case 350
		Return "GrowFlower"
	Case 360
		Return "FloingBubble"
	Case 370
		Return "Crab"
	Case 380
		Return "Ice Troll"
	Case 390
		Return "Kaboom NPC"
	Case 400
		Return "Baby Boomer"
	Case 410
		Return "Flipbridge"
	Case 420
		Return "Coily"
	Case 421
		Return "Scouge"
	Case 422
		Return "Retro UFO"
	Case 423
		Return "Retro Z-Bot"
	Case 424
		Return "Laser Gate"
	Case 425
		Return "Rainbow Coin"
	Case 426
		Return "RetroTollgate"
	Case 430
		Return "Zipbot"
	Case 431
		Return "Zapbot"
	Case 432
		Return "Moobot"
	Case 433
		Return "Z-Bot NPC"
	Case 434
		Return "Mothership"
	Case 441
		Return "Sun Sphere 1"
	Case 442
		Return "Sun Sphere 2"
	Case 450
		Return "Lurker"
	Case 460
		Return "Burstflower"
	Case 470
		Return "Ghost"
	Case 471
		Return "Wraith"
	Case 472
		Return "MODDED NPC"
	Case 805
		Return "IceBlk Again?"

	Default
		Return "NotVanilla"

	End Select

End Function

Function GetDirectionString$(DisplayedRotation)

	If DisplayedRotation=0
		Return "North"
	ElseIf DisplayedRotation=45
		Return "Northeast"
	ElseIf DisplayedRotation=90
		Return "East"
	ElseIf DisplayedRotation=135
		Return "Southeast"
	ElseIf DisplayedRotation=180
		Return "South"
	ElseIf DisplayedRotation=225
		Return "Southwest"
	ElseIf DisplayedRotation=270
		Return "West"
	ElseIf DisplayedRotation=315
		Return "Northwest"
	Else
		Return DisplayedRotation
	EndIf

End Function

Function GetCommandColor(id,index)

	dark=100

	Select id
	Case 5 ; death
		r=GetAnimatedFlashing(LevelTimer)
		g=60
		b=60
	Case 7,8,101,102,103,104,115,120 ; change level
		; red
		r=255
		g=0
		b=0
	Case 21,22,23,24,25,26,27,28,29,30,118 ; dialogs
		; orange
		r=255
		g=dark
		b=0
	Case 61,62,63,64,65 ; NPC manipulation
		; yellow
		r=255
		g=255
		b=0
	Case 1,3 ; change object interactivity
		; green
		r=0
		g=255
		b=0
	Case 2
		; dark green
		r=0
		g=dark
		b=0
	Case 6,9,10,11,12,13,14,16 ; environment
		; cyan
		r=0
		g=255 ;200 ;120 ;dark ;255
		b=255
	Case 4,17,18,51,52 ; alter object attributes / dark magic
		; indigo
		r=0
		g=100
		b=255

		; #e079cb
		;r=224 ;0
		;g=121 ;0
		;b=203 ;255
	Case 15,41,42,119 ; object spawning
		; purple
		r=255
		g=0
		b=255
	Case 111,112,113,114,116,117 ; global state
		; white
		r=255
		g=255
		b=255
	Default ; not a valid command
		; gray
		r=dark
		g=dark
		b=dark
	End Select

	If index=0
		Return r
	ElseIf index=1
		Return g
	Else
		Return b
	EndIf

End Function
