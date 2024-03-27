Function CreateObjectModel(this.woi,complete=False,showHidden=False,inPlayer=True) 

	If inPlayer=False And this=MyCurrentObject
	; First we must clean up MyCurrentObject rendering if we're working on it
		If this\HatEntity>0
			FreeEntity this\HatEntity
			this\HatEntity=0
		EndIf
		If this\AccEntity>0
			FreeEntity this\AccEntity
			this\AccEntity=0
		EndIf
		If this\HatTexture>0
			FreeTexture this\HatTexture
			this\HatTexture=0
		EndIf
		If this\AccTexture>0
			FreeTexture this\AccTexture
			this\AccTexture=0
		EndIf
		If this\Entity>0
			FreeEntity this\Entity
			this\Entity=0
		EndIf
		If this\Texture<>0 
			; FreeTexture this\Texture ; not sure if it cleans the global one from memory or not
			this\Texture=0
		EndIf
	EndIf

	Select this\ModelName$
	; WA1
		Case "!None"
			If showHidden=True
				this\Entity=CreateSphere()
				ScaleEntity this\Entity,.3,.3,.3
			Else
				this\Entity=CreatePivot()
			EndIf
		Case "!Player"
			If complete=True 
				NofObjects=NofObjects+1
				CreateStinkerModel(this,this\XScale,this\YScale,this\ZScale,this\TextureName$)
			EndIf
			If inPlayer=True
				Return -1
			EndIf
		Case "!Hat","!Shadow"
			If inPlayer=True
				Return -1 ; don't create - this is done in CreatePlayer
			EndIf
		; Q - player NPC functionality. model definition copies relevant data from the player's variables upon creation
		Case "!Stinker","!NPC","!PlayerNPC"
			; adding trailing zeros could be easily outsmarted by trimming strings after adding to a power of 10
			If inPlayer=True
				If this\ModelName$="!PlayerNPC"
					dtex=0
					if this\ObjType=120 dtex=8
					this\ObjData[dtex] = PlayerTextureBody	; override body texture
					dhat=2
					if this\ObjType=120 dhat=7
					this\ObjData[dhat] = PlayerAcc1	; override hat model
					this\ObjData[3] = PlayerTexAcc1	; override hat texture
					this\ObjData[4] = PlayerAcc2	; override accessory model
					; Q - getting "texture doesn't exist errors without the subtraction here. is the Episode check below (ln 92) correct? i'm not sure it is
					this\ObjData[5] = PlayerTexAcc2-1	; override accessory texture
					; override model scale
					this\XScale = PlayerSizeX
					this\YScale = PlayerSizeY
					this\ZScale = PlayerSizeZ
				EndIf
				; Create Texture/Accessory Code 
				; !T000b000a0 000a0
				;		T000 - which body texture, b - which expression
				;		000 - which accessory model, a - which accessory texture for that model, 0 - which bone to attach to
				
				NPCTextureCode$="!T"
				dtex=0
				If this\ObjType=120 dtex=8
				NPCTextureCode$=NPCTextureCode$+Right$(Str$(1000+Abs(this\ObjData[dtex])),3)
				NPCTextureCode$=NPCTextureCode$+Chr$(65+this\ObjData[1])
				dhat=2
				If this\ObjType=120 dhat=7
				If this\ObjData[dhat]>0
					NPCTextureCode$=NPCTextureCode$+Right$(Str$(1000+Abs(this\ObjData[dhat])),3)
					NPCTextureCode$=NPCTextureCode$+Chr$(64+this\ObjData[3])+"0"
				EndIf
				If this\ObjData[4]>0 And this\ObjData[dhat]>0
					NPCTextureCode$=NPCTextureCode$+" "
				EndIf
				If this\ObjData[4]>0
					NPCTextureCode$=NPCTextureCode$+Right$(Str$(1000+Abs(this\ObjData[4])),3)
					If WAEpisode=1 Or (AdventureVersion$="096" And AdventureVersionManual$="true")
						NPCTextureCode$=NPCTextureCode$+Chr$(64+this\ObjData[5])+"0"
					Else
						NPCTextureCode$=NPCTextureCode$+Chr$(65+this\ObjData[5])+"0"
					EndIf
				EndIf
				If this\TextureName$="!None" Then this\TextureName$=NPCTextureCode$
				NofObjects=NofObjects+1
				CreateStinkerModel(this,this\XScale,this\YScale,this\ZScale,NPCTextureCode$)
			Else
				this\Entity=CopyEntity(StinkerMesh)
				dhat=2
				If this\ObjType=120 dhat=7
				If this\ObjData[dhat]>0 ; hat ; vanilla editor doesn't show hats for newly created NPCs
					this\HatEntity=MyLoadMesh("Data/Models/stinker/accessory"+Right$(Str$(1000+Abs(this\ObjData[dhat])),3)+".3ds",0)
					this\HatTexture=MyLoadTexture("Data/Models/stinker/accessory"+Right$(Str$(1000+Abs(this\ObjData[dhat])),3)+Chr$(64+this\ObjData[3])+".jpg",4)
					EntityTexture this\HatEntity,this\HatTexture
					ScaleEntity this\HatEntity,this\XScale*this\ScaleAdjust,this\ZScale*this\ScaleAdjust,this\YScale*this\ScaleAdjust
					RotateEntity this\HatEntity,0,0,0
					TurnEntity this\HatEntity,this\PitchAdjust,0,this\RollAdjust
					TurnEntity this\HatEntity,0,this\YawAdjust-90,0
					If this = MyCurrentObject ;i<0
						PositionEntity this\HatEntity,0+this\XAdjust,300+this\ZAdjust+this\Z+.1+.84*this\ZScale/.035,0-this\YAdjust
					Else
						PositionEntity this\HatEntity,this\X+this\XAdjust,this\Z+this\ZAdjust+.1+.84*this\ZScale/.035,-this\Y-this\YAdjust
					EndIf
				EndIf
				If this\ObjData[4]>100 ; acc ; vanilla editor doesn't show accessories for newly created NPCs
					this\AccEntity=MyLoadMesh("Data/Models/stinker/accessory"+Str$(this\ObjData[4])+".3ds",0)
					this\AccTexture=MyLoadTexture("Data/Models/stinker/accessory"+Str$(this\ObjData[4])+Chr$(65+this\ObjData[5])+".jpg",4)
					EntityTexture this\AccEntity,this\AccTexture
					ScaleEntity this\AccEntity,this\XScale*this\ScaleAdjust,this\ZScale*this\ScaleAdjust,this\YScale*this\ScaleAdjust
					RotateEntity this\AccEntity,0,0,0
					TurnEntity this\AccEntity,this\PitchAdjust,0,this\RollAdjust
					TurnEntity this\AccEntity,0,this\YawAdjust-90,0
					If this = MyCurrentObject ;i<0
						PositionEntity this\AccEntity,0+this\XAdjust,300+this\ZAdjust+this\Z+.1+.84*this\ZScale/.035,0-this\YAdjust
					Else
						PositionEntity this\AccEntity,this\X+this\XAdjust,this\Z+this\ZAdjust+.1+.84*this\ZScale/.035,-this\Y-this\YAdjust
					EndIf
				EndIf
			EndIf
			dtex=0
			If this\ObjType=120 dtex=8
			expr=this\ObjData[1]
			If this\ObjType=120 expr=0
			If this\ObjData[dtex]=5
				EntityTexture GetChild(this\Entity,3),WaterFallTexture(0)
			Else If this\ObjData[dtex]=6
				EntityTexture GetChild(this\Entity,3),WaterFallTexture(1)
			Else If Left(this\TextureName$,1)="?" Or (this\TextureName$<>"" And Left$(this\TextureName$,1)<>"!" And this\ModelName$<>"!Button")
				ApplyCustomObjectTexture(GetChild(this\Entity,3),this\TextureName$)
			Else
				EntityTexture GetChild(this\Entity,3),StinkerTexture(this\ObjData[dtex]-1,expr)
			EndIf
			If inPlayer=True
				Return -1
			EndIf
		Case "!StinkerWee"
			this\Entity=CopyEntity(StinkerWeeMesh)
			If inPlayer=False
				EntityTexture this\Entity,StinkerWeeTexture(this\ObjData[8])
			EndIf
		Case "!StinkerWee2" ;useless, will be removed
			If inPlayer=True
				NofObjects=NofObjects+1
				CreateStinkerModel(this,this\XScale,this\YScale,this\ZScale,"!T0080001d0 ")
				Return -1
			Else
				this\Entity=CopyEntity(StinkerMesh)
				this\HatEntity=MyLoadMesh("Data/Models/stinker/accessory001.3ds",0)
				this\HatTexture=MyLoadTexture("Data/Models/stinker/accessory001d.jpg",4)
				EntityTexture this\HatEntity,this\HatTexture
				ScaleEntity this\HatEntity,this\XScale*this\ScaleAdjust,this\ZScale*this\ScaleAdjust,this\YScale*this\ScaleAdjust
				RotateEntity this\HatEntity,0,0,0
				TurnEntity this\HatEntity,this\PitchAdjust,0,this\RollAdjust
				TurnEntity this\HatEntity,0,this\YawAdjust-90,0
				If this = MyCurrentObject ;i<0
					PositionEntity this\HatEntity,0+this\XAdjust,300+this\ZAdjust+this\Z+.1+.84*this\ZScale/.035,0-this\YAdjust
				Else
					PositionEntity this\HatEntity,this\X+this\XAdjust,this\Z+this\ZAdjust+.1+.84*this\ZScale/.035,-this\Y-this\YAdjust
				EndIf
				If this\ObjData[0]=5
					EntityTexture GetChild(this\Entity,3),WaterFallTexture(0)
				Else If this\ObjData[0]=6
					EntityTexture GetChild(this\Entity,3),WaterFallTexture(1)
				Else
					EntityTexture GetChild(this\Entity,3),StinkerTexture(0,3)
				EndIf
			EndIf
			
			;If inPlayer=True
			;	If this\HatEntity<>0
			;		DebugLog "hat"
			;		EntityParent this\HatEntity,FindChild(this\Entity,"hat_bone")
			;	EndIf
			;	If this\AccEntity<>0
			;		EntityParent this\AccEntity,this\Entity
			;	EndIf
			;EndIf
		Case "!Cage"
			this\Entity=CopyEntity(CageMesh)
		Case "!Scritter"
			this\Entity=CopyEntity(ScritterMesh)
			If inPlayer=True And (WAEpisode=1 Or WAEpisode=2 Or AdventureVersion$="096")
				; blue texture for scritters in WA1/WA2
				EntityTexture this\Entity,ScritterTexture(5)
			Else
				EntityTexture this\Entity,ScritterTexture(this\ObjData[0])
			EndIf
		Case "!Button"
			If showHidden=True
				If this\SubType=16 And this\ObjData[2]=1
					this\SubType=17
				EndIf
				If this\SubType=17 And this\ObjData[2]=0
					this\SubType=16
				EndIf
				If this\SubType=16+32 And this\ObjData[2]=1
					this\SubType=17+32 ; +32 for invisible
				EndIf
				If this\SubType=17+32 And this\ObjData[2]=0
					this\SubType=16+32 ; +32 for invisible
				EndIf
				this\Entity=CreateButtonMesh(this\SubType,this\ObjData[0],this\ObjData[1],this\ObjData[2],this\ObjData[3])
			Else
				; If this\SubType=13 And AdventureCompleted(this\ObjData[0])=1
				; 	this\SubType=14 ; player/adventures/objects-enginebase.bb is the right place to do that, moved to ControlButton()
				; EndIf
				If this\SubType=11 Or (this\SubType=15 And this\TextureName$="!None") Or this\SubType>31
					this\Entity=CreatePivot() ; NPC commands or invisible
				Else
					this\Entity=CreateButtonMesh(this\SubType,this\ObjData[0],this\ObjData[1],this\ObjData[2],this\ObjData[3])
				EndIf
			EndIf
		Case "!Teleport"
			this\Entity=CreateTeleporterMesh(this\ObjData[0],this\TextureName$, this)
		Case "!Item" ; crystal-style item from WA1 protos
			this\Entity=CreatePickUpItemMesh(this\ObjData[2])
		Case "!Chomper"
			this\Entity=CopyEntity(ChomperMesh)
			If inPlayer=True
				AnimateMD2 this\Entity,1,.6,1,29
			EndIf
			If this\SubType=1 
				EntityTexture this\Entity,WaterChomperTexture
			Else If this\ObjData[1]=3
				EntityTexture this\Entity,MechaChomperTexture
			Else
				EntityTexture this\Entity,ChomperTexture
			EndIf
		Case "!Bowler"
			this\Entity=CopyEntity(BowlerMesh)
			If inPlayer=False
				this\YawAdjust=(-45*this\ObjData[0] + 3600) Mod 360 ; in vanilla only on level load
			EndIf
		Case "!Turtle"
			this\Entity=CopyEntity(TurtleMesh)
			If this\SubType=1 Then EntityTexture this\Entity,LavaTurtleTex
			If this\ObjData[4]>0 Then EntityTexture this\Entity,RoboTurtleTex
			If inPlayer=True
				If this\Status=2
					AnimateMD2 this\Entity,3,.2,31,49
				EndIf
			Else
				this\YawAdjust=(-90*this\ObjData[0] + 3600) Mod 360 ; in vanilla only on level load
			EndIf
		Case "!Thwart"
			this\Entity=CopyEntity(ThwartMesh)
			EntityTexture this\Entity,ThwartTexture(this\ObjData[2]) ; used Data0 for new objects (???)
		Case "!FireFlower"
			this\Entity=CopyEntity(FireFlowerMesh)
			If inPlayer=False
				If this\SubType<>1
					this\YawAdjust=(-45*this\ObjData[0] + 3600) Mod 360 ; in vanilla only on level load
				Else
					this\YawAdjust=0
				EndIf
			EndIf
			If this\ObjData[1]=1
				EntityTexture this\Entity,FireFlowerTexture2
			EndIf
		Case "!Busterfly"
			this\Entity=CopyEntity(BusterflyMesh)
			If inPlayer=True
				AnimateMD2 this\Entity,2,.4,2,9
			EndIf
		Case "!Rubberducky"
			this\Entity=CopyEntity(RubberDuckyMesh)
		Case "!Void"
			this\Entity=CreateVoidMesh()
		Case "!Spring"
			If inPlayer=True And WAEpisode<>0
				; default spring for WA1/WA2/WA3
				this\Entity=MyLoadMesh("data/models/bridges/cylinder1.b3d",0)
				RotateMesh this\Entity,90,0,0
				ScaleMesh this\Entity,1.1,1.1,1.1
				EntityTexture this\Entity,SpringTexture
			Else
				this\Entity=MyCreateCylinder()
				ScaleMesh this\Entity,0.497,0.497,0.497 ; vanilla cylinder1.b3d is slightly smaller than 1x1x1
				PositionMesh this\Entity,0,0.5,0
				RotateMesh this\Entity,90,0,0
				ScaleMesh this\Entity,1.1,1.1,1.1
				If inPlayer=False
					this\YawAdjust=(-45*this\ObjData[2] + 3600) Mod 360 ; in vanilla only on level load
				EndIf
				TextureSpringMesh(this\Entity,this\ObjData[0])
			EndIf
		Case "!Obstacle10"
			this\Entity=CopyEntity(ObstacleMesh(10))
			EntityTexture this\Entity,MushroomTex((Abs(this\ObjData[0])) Mod 3)
		Case "!Obstacle51","!Obstacle55","!Obstacle59"
			this\Entity=CopyEntity(  ObstacleMesh((Asc(Mid$(this\ModelName$,10,1))-48)*10+(Asc(Mid$(this\ModelName$,11,1))-48)+this\ObjData[0])  )
			EntityTexture this\Entity, ObstacleTexture((Asc(Mid$(this\ModelName$,10,1))-48)*10+(Asc(Mid$(this\ModelName$,11,1))-48)+this\ObjData[1])
		Case "!WaterFall"
			this\Entity=CreateWaterFallMesh(this\ObjData[0])
		Case "!Star"
			this\Entity=CopyMesh(StarMesh)
			EntityTexture this\Entity,GoldStarTexture
		Case "!Wisp"
			this\Entity=CopyMesh(StarMesh)
			EntityTexture this\Entity,WispTexture(this\ObjData[0])
		Case "!GlowWorm","!Zipper"
			this\Entity=CreateSphere(12)
			ScaleMesh this\Entity,.1,.1,.1
			EntityColor this\Entity,this\ObjData[5],this\ObjData[6],this\ObjData[7]
			EntityBlend this\Entity,3
		Case "!Coin"
			this\Entity=CopyMesh(CoinMesh)
			If this\ObjType=425
				EntityTexture this\Entity,RetroRainbowCoinTexture
			Else
				EntityTexture this\Entity,GoldCoinTexture
			EndIf
		Case "!Token"
			this\Entity=CopyMesh(CoinMesh)
			EntityTexture this\Entity,TokenCoinTexture
		Case "!Gem"
			; If this\ObjData[0]<0 Or this\ObjData[0]>2 Then this\ObjData[0]=0 ; !!! TOYING WITH DATA DOES NOT BELONG HERE !!!
			; If this\ObjData[1]<0 Or this\ObjData[1]>15 Then this\ObjData[1]=0 ; !!! TOYING WITH DATA DOES NOT BELONG HERE !!!
			this\Entity=CopyEntity(GemMesh(Abs(this\ObjData[0] Mod 3))) ; simple solution for the above
			If this\TextureName$="!None" 
				If this\ObjData[1]>=0 And this\ObjData[1]<=8
					EntityTexture this\Entity,OldTeleporterTexture(this\ObjData[1])
				Else
					ApplyTeleporterTexture(this\Entity,this\ObjData[1])
				EndIf
			EndIf
		Case "!CustomItem"
			this\Entity=CreateCustomItemMesh(this\ObjData[0])
		Case "!Sign"
			this\Entity=CopyEntity(SignMesh(this\ObjData[0]))
			EntityTexture this\Entity,SignTexture(this\ObjData[1])
		Case "!Barrel1"
			this\Entity=CopyEntity(BarrelMesh)
			EntityTexture this\Entity,BarrelTexture1
			If (this\SubType=1)
				If this\ObjData[0]=0 Then EntityTexture this\Entity,BarrelTextureRainbow(0)
				If this\ObjData[0]=1 Then EntityTexture this\Entity,BarrelTextureRainbow(1)
				If this\ObjData[0]=2 Then EntityTexture this\Entity,BarrelTextureRainbow(2)
				If this\ObjData[0]=3 Then EntityTexture this\Entity,BarrelTextureRainbow(3)
				If this\ObjData[0]=4 Then EntityTexture this\Entity,BarrelTextureRainbow(4)
				If this\ObjData[0]=5 Then EntityTexture this\Entity,BarrelTextureRainbow(5)
				If this\ObjData[0]=6 Then EntityTexture this\Entity,BarrelTextureRainbow(6)
				If this\ObjData[0]=7 Then EntityTexture this\Entity,BarrelTextureRainbow(7)
				If this\ObjData[0]=8 Then EntityTexture this\Entity,BarrelTextureRainbow(8)
			EndIf
		Case "!Barrel2"
			this\Entity=CopyEntity(BarrelMesh)
			EntityTexture this\Entity,BarrelTexture2
		Case "!Barrel2b"
			this\Entity=CopyEntity(BarrelMesh)
			EntityTexture this\Entity,NitroGenTex
		Case "!Cuboid"
			; If this\ObjData[0]<0 Or this\ObjData[0]>8 Then this\ObjData[0]=0 ; !!! TOYING WITH DATA DOES NOT BELONG HERE !!!
			this\Entity=CreateCube()
			ScaleMesh this\Entity,0.4,0.4,0.4
			PositionMesh this\Entity,0,0.5,0
			If this\TextureName$="!None" Then ApplyTeleporterTexture(this\Entity,this\ObjData[0])
		Case "!Cylinder"
			this\Entity=MyCreateCylinder()
			ScaleMesh this\Entity,0.497,0.497,0.497 ; vanilla cylinder1.b3d is slightly smaller than 1x1x1
			PositionMesh this\Entity,0,0.5,0
		Case "!Square"
			this\Entity=CopyMesh(Square)
		Case "!ColourGate"
			this\Data10=0 ; Uses to refresh texture for some reason its buggy otherwise
			tex1=this\ObjData[0]
			tex2=this\ObjData[5]
			;If this\ObjData[4]>=2
			;	case0 = 
			;	If this\Status=0 Or this\Status=1 Then tex1 = tex1 +16
			;	If this\Status=0 Or this\Status=2 Then tex2 = tex2 +16
			;	
			;EndIf
			this\Entity=CreateColourGateMesh(this\ObjData[2],tex1,this\ObjData[4],tex2)
			If inPlayer=False And (this\Active Mod 2)=0 ; EDITOR-ONLY: display inactive gates as lowered
				PositionMesh this\Entity,0,-.99,0
				PositionMesh FindChild(this\Entity,"ColourGateColor"),0,-.99,0
			EndIf
		Case "!Transporter"
			this\Entity=CreateTransporterMesh(this\ObjData[0],3,this\ObjData[3]+1)
			If inPlayer=False
				RotateMesh this\Entity,0,90*this\ObjData[2],0
			EndIf
		Case "!Key"
			this\Entity=CreateKeyMesh(this\ObjData[0])
		Case "!SteppingStone"
			If this\ObjData[0]<0 Or this\ObjData[0]>3 Or this\SubType=1
				this\Entity=MyCreateCylinder()
				ScaleMesh this\Entity,0.497,0.497,0.497 ; vanilla cylinder1.b3d is slightly smaller than 1x1x1
				PositionMesh this\Entity,0,0.5,0
				TextureSteppingStoneMesh(this\Entity,this\ObjData[0]+8)
			Else 
				this\Entity=CopyEntity(SteppingStoneCylinder)
				EntityTexture this\Entity, SteppingStoneTexture(this\ObjData[0])
			EndIf			
		Case "!Door"
			this\Entity=CopyEntity(Door013ds)
		Case "!Fence1"
			this\Entity=CopyEntity(Fence1)
		Case "!Fence2"
			this\Entity=CopyEntity(Fence2)
		Case "!FencePost","!Fencepost"
			this\Entity=CopyEntity(FencePost)
		Case "!Fountain"
			this\Entity=CopyEntity(Fountain)
		Case "!SpellBall"
			If complete=True 
				this\Entity=CreateSpellBallMesh(this\SubType)
			Else
				Return -1
			EndIf
		Case "!IceFloat"
			this\Entity=CreateIceFloatMesh()
		Case "!IceBlock"
			this\Entity=CreateIceBlockMesh(this\ObjData[3])
	; WA2
		Case "!Tentacle"
			this\Entity=CopyEntity(TentacleMesh)
			If inPlayer=True
				Animate GetChild(this\Entity,3),1,.1,1,0
				If this\SubType=1
					For i=1 To CountChildren(this\Entity)
						EntityTexture GetChild(this\Entity,i),TentacleReverseTexture
					Next
				EndIf
			Else
				If this\SubType=1
					EntityTexture this\Entity,TentacleReverseTexture
				EndIf
			EndIf
		Case "!Crab"
			this\Entity=CopyEntity(CrabMesh)
			If this\SubType=0
				EntityTexture this\Entity,CrabTexture2
			EndIf
			If this\SubType=2
				EntityTexture this\Entity,IceCrabTex
			EndIf
			If inPlayer=True
				If this\ObjData[1]>=2
					AnimateMD2 this\Entity,3,1,48,49 ; sleeping
				EndIf
				If this\SubType=0
					EntityTexture this\Entity,CrabTexture2
				EndIf
				If this\SubType=2
					EntityTexture this\Entity,IceCrabTex
				EndIf
				If this\Status=2
					; in water
					this\Z=-.1
					AnimateMD2 this\Entity,3,1,46,49
				EndIf
			EndIf
		Case "!Troll"
			this\Entity=CopyEntity(TrollMesh)
			If inPlayer=True
				AnimateMD2 this\Entity,2,0.005,81,82
			EndIf
		Case "!Kaboom"
			this\Entity=CopyEntity(KaboomMesh)
			EntityTexture this\Entity,KaboomTexture(this\ObjData[0]-1)
			If inPlayer=True
				TurnEntity this\Entity,0,90,0
			EndIf
			
		Case "!KaboomRTW"
			this\Entity=CopyEntity(KaboomRTWMesh)
			EntityTexture this\Entity,KaboomRTWTexture
			If inPlayer=True
				TurnEntity this\Entity,0,90,0
			EndIf
			
		Case "!BabyBoomer"
			this\Entity=CopyEntity(KaboomMesh)
			EntityTexture this\Entity,KaboomTexture(0)
			If inPlayer=True
				TurnEntity this\Entity,0,90,0
			EndIf
		Case "!FlipBridge"
			this\Entity=CreateFlipBridgeMesh(this\Active,this\ObjData[0])
			If inPlayer=True
				IsThereAFlipBridge=True
			Else
				this\YawAdjust=(-45*this\ObjData[2] + 3600) Mod 360 ; in vanilla only on level load
			EndIf
		Case "!GrowFlower"
			If this\ObjData[0]=2
				this\Entity=CopyEntity(ObstacleMesh(7))
			Else If this\ObjData[0]=11 Or this\ObjData[0]=12
				this\Entity=CopyEntity(ObstacleMesh(16))
				;EntityTexture this\Entity,WaterFallTexture
			Else
				this\Entity=CopyEntity(ObstacleMesh(10))
			EndIf
		Case "!FloingBubble"
			this\Entity=CreateSphere()
			s=CreateCylinder()
			ScaleMesh s,0.5,0.01,0.5
			PositionMesh s,0,-0.58,0
			AddMesh s,this\Entity
			FreeEntity s
			EntityTexture this\Entity,FloingTexture
			EntityAlpha this\Entity,0.5
			EntityBlend this\Entity,3
		Case "!FlashBubble"
			this\Entity=CreateSphere()
			s=CreateCylinder()
			ScaleMesh s,0.5,0.01,0.5
			PositionMesh s,0,-0.58,0
			AddMesh s,this\Entity
			FreeEntity s
			EntityTexture this\Entity,Flashbubble
			EntityAlpha this\Entity,0.5
			EntityBlend this\Entity,3
		Case "!FloingOrb"
			If inPlayer=True
				this\Entity=CreatePivot()
			Else
				this\Entity=CreateSphere()
				ScaleMesh this\Entity,.3,.3,.3
				EntityColor this\Entity,255,0,0
			EndIf
		Case "!MagicMirror"
			this\Entity=CreateCube()
			ScaleMesh this\Entity,3.49,2.49,.52
			If inPlayer=False
				EntityColor this\Entity,255,0,0
				EntityAlpha this\Entity,.5
			EndIf
		Case "!Retrobox"
			this\Entity=CopyEntity(RetroBoxMesh)
		Case "!Retrocoily"
			this\Entity=CopyEntity(RetroCoilyMesh)
		Case "!Retroscouge"
			this\Entity=CopyEntity(RetroScougeMesh)
			If this\SubType=1 Then EntityTexture this\Entity,RetroScougeTexture2
			this\YawAdjust=(-90*this\ObjData[0] + 3600) Mod 360
		Case "!Retrozbot"
			this\Entity=CopyEntity(RetroZBotMesh)
			this\YawAdjust=(-90*this\ObjData[0] + 3600) Mod 360
		Case "!Retroufo"
			this\Entity=CopyEntity(RetroUfoMesh)
			this\YawAdjust=(-90*this\ObjData[0] + 3600) Mod 360
		Case "!Retrolasergate"
			this\Entity=CreateRetroLaserGateMesh(this\ObjData[0])
			If inPlayer=False And (this\Active Mod 2)=0 ; EDITOR-ONLY: display inactive laser gates as semi-trnsparent
				EntityAlpha this\Entity,.6
			EndIf
		Case "!SkyMachineMap"
			this\Entity=CreateCube()
			ScaleMesh this\Entity,2.5,.01,2.5
			PositionMesh this\Entity,0,0,-1
			EntityTexture this\Entity,SkyMachineMapTexture
			EntityBlend this\Entity,3
	; WA3
		Case "!BurstFlower"
			this\Entity=CopyEntity(BurstFlowerMesh)
		Case "!StarGate"
			this\Entity=CopyEntity(StarGateMesh)
		Case "!Autodoor"
			this\Entity=CopyEntity(AutoDoorMesh)
			If inPlayer=True
				If this\TileX=0
					this\TileX=Floor(this\X)
					this\TileY=Floor(this\Y)
				EndIf
			EndIf
		Case "!RainbowBubble"
			this\Entity=CreateSphere()
			ScaleMesh this\Entity,.5,.5,.5
			If inPlayer=False
				PositionMesh this\Entity,0,1,0
			EndIf
			EntityTexture this\Entity,RainbowTexture2
		Case "!PlantFloat"
			this\Entity=CreatePlantFloatMesh()
		Case "!Lurker"
			this\Entity=CopyEntity(LurkerMesh)
		Case "!Ghost"
			this\Entity=CopyEntity(GhostMesh)
			EntityFX this\Entity,1
		Case "!Wraith"
			this\Entity=CopyEntity(WraithMesh)
			If this\ObjData[2]<3
				EntityTexture this\Entity,WraithTexture(this\ObjData[2])
			Else
				EntityTexture this\Entity,NewWraithTexture(this\ObjData[2]-3)
			EndIf
			EntityFX this\Entity,1
		Case "!Suctube"
			this\Entity=CreateSucTubeMesh(this\ObjData[3],this\ObjData[0],this\Active)
			If inPlayer=True
				RedoSucTubeMesh(this)
			Else
				this\YawAdjust=(-90*this\ObjData[2] + 3600) Mod 360 ; in vanilla only on level load
			EndIf
		Case "!SuctubeX"
			this\Entity=CreateSucTubeXMesh(this\ObjData[3])
			If inPlayer=False
				this\YawAdjust=(-90*this\ObjData[2] + 3600) Mod 360 ; in vanilla only on level load
			EndIf
		Case "!Portal Warp"
			this\Entity=CopyEntity(PortalWarpMesh)
			If this\ObjData[1]=0
				EntityTexture this\Entity,StarTexture
				EntityAlpha this\Entity,.5
			Else
				EntityTexture this\Entity,RainbowTexture
				EntityAlpha this\Entity,.5
			EndIf
		Case "!Sun Sphere1"
			this\Entity=CreateSphere()
			If inPlayer=False
				PositionMesh this\Entity,0,1.5,0
			EndIf
			EntityColor this\Entity,this\ObjData[0],this\ObjData[1],this\ObjData[2]
			EntityBlend this\Entity,3
		Case "!Sun Sphere2"
			this\Entity=CreateSphere()
			ScaleMesh this\Entity,.5,.5,.5
		Case "!Crystal"
			this\Entity=CopyEntity(GemMesh(2))
			If this\ObjData[0]=0
				EntityTexture this\Entity,RainbowTexture
			Else
				EntityTexture this\Entity,GhostTexture
			EndIf
		Case "!Conveyor"
			convsize#=1.0
			If this\ObjType=45 
				convsize=1.1
			Else
				convsize=0.7
			EndIf
			If this\ObjData[4]=4
				this\Entity=CreateCloudMesh(this\ObjData[0],convsize)
			Else
				If inPlayer=True
					If this\ObjData[4]<3
						this\Entity=CreateTransporterMesh(this\ObjData[0],this\ObjData[4],0)
					Else
						this\Entity=CreateTransporterMesh(this\ObjData[0],this\ObjData[4],1)
					EndIf
					RotateMesh this\Entity,0,90*this\ObjData[2],0
					If this\ObjType=46
						this\XScale=.8
						this\YScale=.8
					EndIf
				Else
					If this\ObjType=45
						turn=this\ObjData[3]+2
					Else
						turn=1
					EndIf
					this\Entity=CreateTransporterMesh(this\ObjData[0],this\ObjData[4],turn)
					If this\ObjType=46
						ScaleMesh this\Entity,.5,.5,.5
						If FindChild(this\Entity,"TransporterColour")<>0
							ScaleMesh FindChild(this\Entity,"TransporterColour"),.5,.5,.5
						EndIf
					EndIf
				EndIf
				this\YawAdjust=-90.0*this\ObjData[2]
			EndIf
		Case "!KeyCard"
			If this\ObjData[0]>=0 And this\ObjData[0]<=15
				; use new method
				this\Entity=CreateKeyCardMesh(this\ObjData[0])
			Else
				; use old method
				this\Entity=CreateOldKeyCardMesh(this\ObjData[0])
			EndIf
		Case "!Weebot"
			this\Entity=CopyEntity(WeeBotMesh)
			this\YawAdjust=(-90*this\ObjData[0] + 3600) Mod 360
		Case "!Zapbot"
			this\Entity=CopyEntity(ZapbotMesh)
			this\YawAdjust=(-90*this\ObjData[0] + 3600) Mod 360
		Case "!Pushbot"
			this\Entity=CreatePushbotMesh(this\ObjData[0],this\ObjData[3])
			If (this\SubType=1 And this\ObjData[8]=0) Then EntityTexture this\Entity,KillerMoobot
			;If (this\SubType=1 And this\ObjData[8]=1) Then this\ScaleAdjust=1.5 <------ doesnt work here?
			If inPlayer=True
				this\YawAdjust=0
			Else
				this\YawAdjust=-90*this\ObjData[2]
			EndIf
		Case "!ZbotNPC"
			this\Entity=CopyEntity(ZBotNPCMesh)
			EntityTexture this\Entity,ZBotNPCTexture(this\ObjData[2])
		Case "!Mothership"
			this\Entity=CopyEntity(MothershipMesh)
	; WAE
		Case "!CustomModel"
			If Left(this\TextData[0],1)<>"?" ; for backward compatiblity only, use ModelName field instead
				modelName$="?"+this\TextData[0]
			Else
				modelName$=this\TextData[0]
			EndIf
			this\Entity=CreateCustomModelMesh(this\ModelName$,modelName$,this.woi)
			If this\TextureName$="!None"
				ApplyCustomObjectTexture(this\Entity,modelName$,"UserData/Custom/Models/",this\SubType,this\ObjType)
			EndIf
		Case "!Barrel3"
			this\Entity=CopyEntity(BarrelMesh)
			EntityTexture this\Entity,BarrelTexture3
		Case "!MagicTurret"
			this\Entity=CopyEntity(MagicTurretMesh)
		Case "!Prism"
			this\Entity=CopyEntity(PrismMesh)
			If this\ObjData[0]<>1
				EntityTexture this\Entity,PrismTexture
			Else
				EntityTexture this\Entity,SuperPrismTexture
			EndIf
		Case "!Vehicle"
			If this\ObjType=463
				If this\SubType=0 Then this\Entity=CreateRotatorMesh(this\ObjData[0])
				If this\SubType=1 Then this\Entity=CreateDestroyerMesh()
			ElseIf this\ObjType=464
				this\Entity=CreateGeneratorMesh(this\Active, this\ObjData[0])
			;ElseIf this\ObjType=461
			;	this\Entity=CreateVehicleMesh(this\ObjData[6], this\ObjData[5])
			;	EntityTexture this\Entity,VehicleTexture
			ElseIf this\ObjType=465
				cube = CreateCube()
				ScaleMesh cube, .5, .5, .5
				EntityTexture cube,GVehicleTexture
				this\Entity=cube
				EntityAlpha cube,0.6
			Else
				this\Entity=CreateVehicleMesh(this\Active, this\ObjData[2], this\ObjData[0])
				EntityTexture this\Entity,VehicleTexture
			EndIf
		Case "!VehicleBox"
		;deprecated, dont use
				cube = CreateCube()
				ScaleMesh cube, .5, .5, .5
				EntityTexture cube,GVehicleTexture
				this\Entity=cube
				EntityAlpha cube,0.6
				
			
			
		Default
			If Left$(this\ModelName$,9)="!Obstacle"
				this\Entity=CopyEntity(ObstacleMesh((Asc(Mid$(this\ModelName$,10,1))-48)*10+(Asc(Mid$(this\ModelName$,11,1))-48)))
			ElseIf WAEpisode=1 Or AdventureVersion$="096"
				; special case for WA1
				this\Entity=MyLoadMesh(this\ModelName$)
				If this\ObjType=163
					PositionMesh this\Entity,0,-5.65/.037,1.25/.037
				EndIf
			Else
				this\Entity=CreateCustomModelMesh(this\ModelName$,this\TextData[0],this.woi)
				If this\TextureName$="!None"
					ApplyCustomObjectTexture(this\Entity,this\ModelName$,"UserData/Custom/Models/",this\SubType,this\ObjType)
				EndIf
			EndIf
	End Select

	; TEXTURE
	Select this\TextureName$
		Case "!Door"
			EntityTexture this\Entity,DoorTexture(this\ObjData[5] Mod 3)
		Case "!Cottage"
			EntityTexture this\Entity,CottageTexture(this\ObjData[5] Mod 2)
		Case "!Townhouse"
			EntityTexture this\Entity,HouseTexture(this\ObjData[5] Mod 3)
			If this\ObjData[5]=2
				EntityFX this\Entity,1 ; fullbright for night texture
			EndIf
		Case "!Windmill"
			EntityTexture this\Entity,WindmillTexture ; Was (this\ObjData[5]) but there's only one texture
		Case "!Fence"
			EntityTexture this\Entity,FenceTexture ; Was (this\ObjData[5]) but there's only one texture
		Case "!FireTrap","Data/Models/Squares/firetrap.bmp"
			EntityTexture this\Entity,FireTrapTexture
		Case "!GloveTex"
			EntityTexture this\Entity,GloveTex
			If inPlayer=False
				EntityFX this\Entity,2
				local rgb[2]
				Select this\ObjType
					Case 200; magic glove
						If this\ObjData[0]=8
							For ii=0 To 3
								rgb[0]=ii*64
								rgb[1]=64+ii*32
								rgb[2]=255-ii*32
								VertexColor GetSurface(this\Entity,1),ii,rgb[0],rgb[1],rgb[2]
							Next
						Else
							For i=0 To 2
								rgb[i] = GetSpellColor(this\ObjData[0],i)
							Next
							For ii=0 To 3
								VertexColor GetSurface(this\Entity,1),ii,rgb[0],rgb[1],rgb[2]
							Next
						EndIf
					Case 201 ; anti-magic glove
						colorMult# = 0.3
						For i=0 To 2
							rgb[i] = GetSpellColor(this\ObjData[0],i) * colorMult
						Next
						For ii=0 To 3
							VertexColor GetSurface(this\Entity,1),ii,rgb[0],rgb[1],rgb[2]
						Next
				End Select
			EndIf
		Default
			If (WAEpisode=1 Or AdventureVersion$="096")  And (this\TextureName$<>"" And Left$(this\TextureName$,1)<>"!" And this\ModelName$<>"!Button")
				EntityTexture this\Entity,MyLoadTexture(this\TextureName$,4)
			Else If Left(this\TextureName$,1)="?" Or (this\TextureName$<>"" And Left$(this\TextureName$,1)<>"!" And this\ModelName$<>"!Button")
				ApplyCustomObjectTexture(this\Entity,this\TextureName$)
			Else If Left$(this\TextureName$,2)="!T"
				EntityTexture this\Entity,StinkerTexture(0,0)
				For k=1 To CountChildren(this\Entity)
					EntityTexture GetChild(this\Entity,k),StinkerTexture(0,0)
				Next
			Else
				this\Texture=0
			EndIf
	End Select

	If inPlayer=True
		Return 0
	EndIf

	If this\ScaleAdjust=0.0
		this\ScaleAdjust=1.0
	EndIf
	If this\ModelName$<>"!None"
		ScaleEntity this\Entity,this\XScale*this\ScaleAdjust,this\ZScale*this\ScaleAdjust,this\YScale*this\ScaleAdjust
		RotateEntity this\Entity,0,0,0
		TurnEntity this\Entity,this\PitchAdjust,0,this\RollAdjust
		TurnEntity this\Entity,0,this\YawAdjust,0
	EndIf
	If this=MyCurrentObject
		PositionEntity this\Entity,0+this\XAdjust,300+this\ZAdjust+this\Z,0-this\YAdjust
	Else
		PositionEntity this\Entity,this\X+this\XAdjust,this\Z+this\ZAdjust,-this\Y-this\YAdjust
	EndIf
	If this\HatEntity<>0
		EntityParent this\HatEntity,FindChild(this\Entity,"hat_bone")
	EndIf
	If this\AccEntity<>0
		EntityParent this\AccEntity,this\Entity
	EndIf
End Function

Function MyCreateCylinder(segments=10,solid=True,cMargin#=0.89062)
	cylinder=CreateCylinder(segments,solid)
	cylinderChild=CopyEntity(cylinder,cylinder)
	NameEntity cylinderChild,"CylinderChild"

	cylinderSides=GetSurface(cylinder,1)
	cylinderChildSides=GetSurface(cylinderChild,1)
	For i=0 To segments
		u# = Abs(2*((i+2) Mod (segments))-segments)/Float(segments)
		v# = cMargin#/Float(100)
		VertexTexCoords cylinderSides,2*i,(1-0.0075*2)*u#+0.0075,10.0*(1.0-v#)
		VertexTexCoords cylinderSides,2*i+1,(1-0.0075*2)*u#+0.0075,10.0*v#
		VertexTexCoords cylinderSides,2*i,(1-0.0075*2)*u#+0.0075,10.0*(1.0-v#),0,1
		VertexTexCoords cylinderSides,2*i+1,(1-0.0075*2)*u#+0.0075,10.0*v#,0,1
		VertexTexCoords cylinderChildSides,2*i,0.5*((1-0.0075*2)*u#+0.0075),1.0-v#
		VertexTexCoords cylinderChildSides,2*i+1,0.5*((1-0.0075*2)*u#+0.0075),v#
	Next
	If solid=True
		cylinderEnds=GetSurface(cylinder,2)
		cylinderChildEnds=GetSurface(cylinderChild,2)
		For j=0 To 2*segments-1
			u# = 0.75+0.5*cMargin#*(VertexV(cylinderEnds,j)-0.5)
			v# = 0.5+cMargin#*(VertexU(cylinderEnds,j)-0.5)
			VertexTexCoords cylinderEnds,j,2.0*u#,v#
			VertexTexCoords cylinderChildEnds,j,u#,v#
		Next
	EndIf
	UpdateNormals cylinder
	UpdateNormals cylinderChild
	Return cylinder
End Function

Function CreateRotatorMesh(data0)
    cube = CreateCube()
	ScaleMesh cube, .5, .5, .5
	EntityTexture cube,VRotatorDefault
	
	device=CreateCube()
	ScaleMesh device, .5, .5, .0001
	PositionMesh device, 0, 0, .5001
	
	If data0=0
		EntityTexture device,VRotateTexture
	ElseIf data0=1
		EntityTexture device,VRotateTexture2
	Else
		EntityTexture device,VRotateTexture3
	EndIf
	RotateEntity device,0,0,180;,0
	EntityParent device,cube
	
    Return cube
End Function

Function CreateDestroyerMesh()
    cube = CreateCube()
	ScaleMesh cube, .5, .5, .5
	EntityTexture cube,VDestroyer
    Return cube
End Function

Function CreateGeneratorMesh(active,data0)
    cube = CreateCube()
	ScaleMesh cube, .5, .5, .5
	EntityTexture cube,VRotatorDefault
	
	device=CreateCube()
	ScaleMesh device, .5, .5, .0001
	PositionMesh device, 0, 0, .5001
	
	NameEntity device, "GeneratingDevice"
	
	RedoGeneratorMesh(device,active,data0)
	EntityParent device,cube
	
    Return cube
End Function

Function RedoGeneratorMesh(Entity,Active,dir)
	If dir=0 Then EntityTexture FindChild(Entity,"GeneratingDevice"),VehicleGen1
	If dir=1 Then EntityTexture FindChild(Entity,"GeneratingDevice"),VehicleGen2
	If dir=2 Then EntityTexture FindChild(Entity,"GeneratingDevice"),VehicleGen3
	If dir=3 Then EntityTexture FindChild(Entity,"GeneratingDevice"),VehicleGen4
End Function

Function CreateVehicleMesh(active,data2,data0)
    cube = CreateCube()
	ScaleMesh cube, .5, .5, .5
	
	device=CreateCube()
	ScaleMesh device, .5, .5, .0001
	PositionMesh device, 0, 0, .5001
	
	NameEntity device, "ArrowPointingDevice"
	
	If data0>-1
		colouroutline1=CreateCube()
		ScaleMesh colouroutline1, .30, .0001, .30
		PositionMesh colouroutline1, 0, .5002, 0
		EntityParent colouroutline1,cube
		EntityTexture colouroutline1, ColorTexture(14)
		colourmesh1=CreateCube()
		ScaleMesh colourmesh1, .25, .0001, .25
		PositionMesh colourmesh1, 0, .5005, 0
		NameEntity colourmesh1, "ColourVehicleColor1"
		EntityParent colourmesh1,cube
		
		colouroutline2=CreateCube()
		ScaleMesh colouroutline2, .0001, .30, .30
		PositionMesh colouroutline2, -.5002, 0, 0
		EntityParent colouroutline2,cube
		EntityTexture colouroutline2, ColorTexture(14)
		colourmesh2=CreateCube()
		ScaleMesh colourmesh2, .0001, .25, .25
		PositionMesh colourmesh2, -.5005, 0, 0
		NameEntity colourmesh2, "ColourVehicleColor2"
		EntityParent colourmesh2,cube
		
		colouroutline3=CreateCube()
		ScaleMesh colouroutline3, .0001, .30, .30
		PositionMesh colouroutline3, .5002, 0, 0
		EntityParent colouroutline3,cube
		EntityTexture colouroutline3, ColorTexture(14)
		colourmesh3=CreateCube()
		ScaleMesh colourmesh3, .0001, .25, .25
		PositionMesh colourmesh3, .5005, 0, 0
		NameEntity colourmesh3, "ColourVehicleColor3"
		EntityParent colourmesh3,cube

		TextureVehicleMesh(colourmesh1,colourmesh2,colourmesh3,data0)
	EndIf
	
	;EntityTexture device,VDeviceTexture
	
	RedoVehicleArrow(device,active,data2)
	EntityParent device,cube
	
    Return cube
End Function

Function RedoVehicleArrow(Entity,Active,dir)
	If Active=True
		EntityTexture FindChild(Entity,"ArrowPointingDevice"),VDeviceTexture
	Else
		EntityTexture FindChild(Entity,"ArrowPointingDevice"),VDeviceTexture2
	EndIf
	If dir=0 Then RotateEntity FindChild(Entity,"ArrowPointingDevice"),0,0,180;,0,0
	If dir=1 Then RotateEntity FindChild(Entity,"ArrowPointingDevice"),0,0,270;,0,0
	If dir=2 Then RotateEntity FindChild(Entity,"ArrowPointingDevice"),0,0,0;,0,0
	If dir=3 Then RotateEntity FindChild(Entity,"ArrowPointingDevice"),0,0,90;,0,0
End Function

Function TextureVehicleMesh(colourvehicle_handle1,colourvehicle_handle2,colourvehicle_handle3,tex=0)
	EntityTexture FindChild(colourvehicle_handle1,"ColourVehicleColor1"),GetColorTexture(tex)
	EntityTexture FindChild(colourvehicle_handle2,"ColourVehicleColor2"),GetColorTexture(tex)
	EntityTexture FindChild(colourvehicle_handle3,"ColourVehicleColor3"),GetColorTexture(tex)
End Function

Function CreateSpellBallMesh(subtype)
	entity=CreateSphere(4)
	; make the spellball lighter than the magic particles
	Local rgb[2]
	For i=0 To 2
		rgb[i]=GetSpellColor(subtype,i)+50
		If rgb[i]>255 Then rgb[i]=255
	Next
	EntityColor(entity,rgb[0],rgb[1],rgb[2])
	ScaleMesh entity,.15,.15,.15
	EntityBlend entity,3
	Return entity
End Function

Function CreateIceBlockMesh(btype)
	; type- 0-ice, 1-floing
	If btype=0
		entity=CreateCube()
		ScaleMesh entity,.52,.75,.52
		PositionMesh entity,0,.75,0
		EntityAlpha entity,.5
		EntityColor entity,100,255,255
		EntityBlend entity,3
	Else If btype=1
		entity=CreateSphere()
		ScaleMesh entity,.8,.8,.8
		PositionMesh entity,0,.6,0
		EntityTexture entity,FloingTexture
		EntityAlpha entity,0.5
		EntityBlend entity,3
	EndIf
	Return entity
End Function

Function CreateVoidMesh()
	levelExitEntity=CreateMesh()
	surface=CreateSurface(levelExitEntity)
	For i=0 To 17
		AddVertex surface,.5*Sin(i*20),1,.5*Cos(i*20),i*.0555,0
		AddVertex surface,.1*Sin(i*20),0,.1*Cos(i*20),i*.0555,1
	Next
	For i=0 To 16
		AddTriangle surface,i*2,i*2+2,i*2+1
		AddTriangle surface,i*2+2,i*2+3,i*2+1
		AddTriangle surface,i*2+1,i*2+2,i*2
		AddTriangle surface,i*2+1,i*2+3,i*2+2
	Next
	AddTriangle surface,34,0,35
	AddTriangle surface,0,1,35
	AddTriangle surface,35,0,34
	AddTriangle surface,35,1,1
	For i=0 To 17
		AddVertex surface,.7*Sin(i*20),.5,.7*Cos(i*20),i*.0555,0
		AddVertex surface,.15*Sin(i*20),0,.15*Cos(i*20),i*.0555,1
	Next
	For i=18 To 34
		AddTriangle surface,i*2,i*2+2,i*2+1
		AddTriangle surface,i*2+2,i*2+3,i*2+1
		AddTriangle surface,i*2+1,i*2+2,i*2
		AddTriangle surface,i*2+1,i*2+3,i*2+2
	Next
	AddTriangle surface,70,36,71
	AddTriangle surface,36,37,71
	AddTriangle surface,71,36,70
	AddTriangle surface,71,37,36
	UpdateNormals levelExitEntity
	EntityBlend levelExitEntity,3
;	EntityAlpha levelExitEntity,.5
	EntityTexture levelExitEntity,VoidTexture
;	PositionEntity levelExitEntity,x+.5,0,-y-.5
	Return levelExitEntity
End Function

Function CreateIceFloatMesh()
	entity=CreateCylinder(16,True)
	;For i=1 To CountVertices (GetSurface(entity,1))-1
	;	VertexCoords GetSurface(entity,1),i,VertexX(GetSurface(entity,1),i)+Rnd(-.1,.1),VertexY(GetSurface(entity,1),i),VertexZ(GetSurface(entity,1),i)+Rnd(-.1,.1)
	;Next
	ScaleMesh entity,.45,.05,.45
	PositionMesh entity,0,-.1,0
	EntityAlpha entity,.8
	;EntityBlend Entity,3
	EntityColor entity,255,255,255
	Return entity
End Function

Function TextureSteppingStoneMesh(steppingstone_handle,tex)
	If (tex>31 Or tex<0) ; could also consider tex=Abs(tex Mod 32)
		tex=0
	EndIf
	
	EntityTexture steppingstone_handle,GetColorTexture(tex)
	If tex<8 Or tex>11
		EntityTexture FindChild(steppingstone_handle,"CylinderChild"),StepStoneTexture
	Else
		EntityTexture FindChild(steppingstone_handle,"CylinderChild"),SteppingStoneTexture(tex-8)
	EndIf
End Function

Function RedoSteppingStoneTexture(this.woi)
	; return if invalid model name
	If this\ModelName$<>"!SteppingStone"
		Return
	EndIf
	TextureSteppingStoneMesh(this\Entity,(this\ID-500)/5)
End Function

Function TextureSpringMesh(spring_handle,tex)
	If (tex>31 Or tex<0) ; could also consider tex=Abs(tex Mod 32)
		tex=0
	EndIf
	EntityTexture spring_handle,GetColorTexture(tex)
	EntityTexture FindChild(spring_handle,"CylinderChild"),SpringTexture
End Function

Function RedoSpringTexture(this.woi)
	; return if invalid model name
	If this\ModelName$<>"!Spring"
		Return
	EndIf
	; run only in editor
	If WAEpisode=0
		TextureSpringMesh(this\Entity,(this\ID-500)/5)
	EndIf
End Function

Function CreateColourGateMesh(subtype=0,tex=0,logic=0,tex2=0)
	mainEntity=CreateMesh()
	mainSurface=CreateSurface(mainEntity)
	; NameEntity colorEntity,"ColourGateMain"
	colorEntity=CreateMesh()
	colorEntity2=CreateMesh()
	colorSurface=CreateSurface(colorEntity)
	colorSurface2=CreateSurface(colorEntity2)
	NameEntity colorEntity,"ColourGateColor"
	NameEntity colorEntity2,"ColourGateColor2"
	; Top 
	AddVertex (mainSurface,-.499,1.0,.499,subtype*0.25+.01,.26)
	AddVertex (mainSurface,.499,1.0,.499,subtype*0.25+.24,.26)
	AddVertex (mainSurface,-.499,1.0,-.499,subtype*0.25+.01,.49)
	AddVertex (mainSurface,.499,1.0,-.499,subtype*0.25+.24,.49)
	AddTriangle (mainSurface,0,1,2)
	AddTriangle (mainSurface,1,3,2)
	
	If logic>=2
		AddVertex (colorSurface,-.25,1.001,.25,0,0);(tex Mod 8)*0.125+.01,(tex/8)*0.125+.51)
		AddVertex (colorSurface,0,1.001,.25,1,0);(tex Mod 8)*0.125+.115,(tex/8)*0.125+.51)
		AddVertex (colorSurface,-.25,1.001,-.25,0,1);(tex Mod 8)*0.125+.01,(tex/8)*0.125+.51+.115)
		AddVertex (colorSurface,0,1.001,-.25,1,1);(tex Mod 8)*0.125+.115,(tex/8)*0.125+.51+.115)
		AddTriangle (colorSurface,0,1,2)
		AddTriangle (colorSurface,1,3,2)
		
		AddVertex (colorSurface2,-0,1.001,.25,0,0);(tex Mod 8)*0.125+.01,(tex/8)*0.125+.51)
		AddVertex (colorSurface2,.25,1.001,.25,1,0);(tex Mod 8)*0.125+.115,(tex/8)*0.125+.51)
		AddVertex (colorSurface2,-0,1.001,-.25,0,1);(tex Mod 8)*0.125+.01,(tex/8)*0.125+.51+.115)
		AddVertex (colorSurface2,.25,1.001,-.25,1,1);(tex Mod 8)*0.125+.115,(tex/8)*0.125+.51+.115)
		AddTriangle (colorSurface2,0,1,2)
		AddTriangle (colorSurface2,1,3,2)
	Else
		AddVertex (colorSurface,-.25,1.001,.25,0,0);(tex Mod 8)*0.125+.01,(tex/8)*0.125+.51)
		AddVertex (colorSurface,.25,1.001,.25,1,0);(tex Mod 8)*0.125+.115,(tex/8)*0.125+.51)
		AddVertex (colorSurface,-.25,1.001,-.25,0,1);(tex Mod 8)*0.125+.01,(tex/8)*0.125+.51+.115)
		AddVertex (colorSurface,.25,1.001,-.25,1,1);(tex Mod 8)*0.125+.115,(tex/8)*0.125+.51+.115)
		AddTriangle (colorSurface,0,1,2)
		AddTriangle (colorSurface,1,3,2)
	EndIf
	
	
	; Sides
	For i=0 To 3
		Select i
		Case 0
			x1#=-.498
			x2#=.498
			y1#=-.498
			y2#=-.498
		Case 1
			x1#=.498
			x2#=.498
			y1#=-.498
			y2#=.498
		Case 2
			x1#=.498
			x2#=-.498
			y1#=.498
			y2#=.498
		Case 3
			x1#=-.498
			x2#=-.498
			y1#=.498
			y2#=-.498
		End Select
		AddVertex (mainSurface,x1,1,y1,subtype*0.25+.01,.01)
		AddVertex (mainSurface,x2,1,y2,subtype*0.25+.24,.01)
		AddVertex (mainSurface,x1,0,y1,subtype*0.25+.01,.24)
		AddVertex (mainSurface,x2,0,y2,subtype*0.25+.24,.24)
		AddTriangle (mainSurface,4+i*4,5+i*4,6+i*4)
		AddTriangle (mainSurface,5+i*4,7+i*4,6+i*4)
		
		If logic>=2 Then
			AddVertex (colorSurface,x1*1.001,.8,y1*1.001,0,0);(tex Mod 8)*0.125+.01,(tex/8)*0.125+.51)
			AddVertex (colorSurface,x2*1.001,.8,y2*1.001,1,0);(tex Mod 8)*0.125+.115,(tex/8)*0.125+.51)
			AddVertex (colorSurface,x1*1.001,.7,y1*1.001,0,1);(tex Mod 8)*0.125+.01,(tex/8)*0.125+.51+.115)
			AddVertex (colorSurface,x2*1.001,.7,y2*1.001,1,1);(tex Mod 8)*0.125+.115,(tex/8)*0.125+.51+.115)
			AddTriangle (colorSurface,4+i*4,5+i*4,6+i*4)
			AddTriangle (colorSurface,5+i*4,7+i*4,6+i*4)
			
			AddVertex (colorSurface2,x1*1.001,.7,y1*1.001,0,0);(tex Mod 8)*0.125+.01,(tex/8)*0.125+.51)
			AddVertex (colorSurface2,x2*1.001,.7,y2*1.001,1,0);(tex Mod 8)*0.125+.115,(tex/8)*0.125+.51)
			AddVertex (colorSurface2,x1*1.001,.6,y1*1.001,0,1);(tex Mod 8)*0.125+.01,(tex/8)*0.125+.51+.115)
			AddVertex (colorSurface2,x2*1.001,.6,y2*1.001,1,1);(tex Mod 8)*0.125+.115,(tex/8)*0.125+.51+.115)
			AddTriangle (colorSurface2,4+i*4,5+i*4,6+i*4)
			AddTriangle (colorSurface2,5+i*4,7+i*4,6+i*4)
		Else
			AddVertex (colorSurface,x1*1.001,.8,y1*1.001,0,0);(tex Mod 8)*0.125+.01,(tex/8)*0.125+.51)
			AddVertex (colorSurface,x2*1.001,.8,y2*1.001,1,0);(tex Mod 8)*0.125+.115,(tex/8)*0.125+.51)
			AddVertex (colorSurface,x1*1.001,.6,y1*1.001,0,1);(tex Mod 8)*0.125+.01,(tex/8)*0.125+.51+.115)
			AddVertex (colorSurface,x2*1.001,.6,y2*1.001,1,1);(tex Mod 8)*0.125+.115,(tex/8)*0.125+.51+.115)
			
			AddTriangle (colorSurface,4+i*4,5+i*4,6+i*4)
			AddTriangle (colorSurface,5+i*4,7+i*4,6+i*4)
		EndIf
		
		
	Next
	If logic=0
		EntityTexture mainEntity,GateTexture
	Else
		EntityTexture mainEntity,LogicGates(logic-1)
	EndIf
	UpdateNormals mainEntity
	UpdateNormals colorEntity
	UpdateNormals colorEntity2
	EntityParent colorEntity,mainEntity
	EntityParent colorEntity2,mainEntity
	TextureColourGateMesh(mainEntity,tex,tex2)
	Return mainEntity
End Function

Function TextureColourGateMesh(colourgate_handle,tex=0,tex2=0)
	EntityTexture FindChild(colourgate_handle,"ColourGateColor"),GetColorTexture(tex)
	EntityTexture FindChild(colourgate_handle,"ColourGateColor2"),GetColorTexture(tex2)
End Function

Function RedoGateTexture(this.woi)
	; return if invalid model name
	If this\ModelName$<>"!ColourGate"
		Return
	EndIf
	tex1 = (this\ID-500)/5;
	tex2 = this\ObjData[5];
	
	If this\ObjData[4]>=2
		If this\Status=0 Or this\Status=1 Then tex1 = tex1 +16
		If this\Status=0 Or this\Status=2 Then tex2 = tex2 +16
	EndIf
	
	TextureColourGateMesh(this\Entity,tex1,tex2)
End Function

Function CreateFlipBridgeMesh(active=0,tex=0,range=3)
	subtype=3
	mainEntity=CreateMesh()
	mainSurface=CreateSurface(mainEntity)
	; NameEntity mainEntity,"FlipBridgeMain"
	; Top 
	AddVertex (mainSurface,-.25,.1,.49,.76,.01)
	AddVertex (mainSurface,.25,.1,.49,.76+.24,.01)
	AddVertex (mainSurface,-.25,.1,-.49,.76,.24)
	AddVertex (mainSurface,.25,.1,-.49,.76+.24,.24)
	AddTriangle (mainSurface,0,1,2)
	AddTriangle (mainSurface,1,3,2)
	; Sides
	For i=0 To 3
		Select i
		Case 0
			x1#=-.25
			x2#=.25
			y1#=-.49
			y2#=-.49
		Case 1
			x1#=.25
			x2#=.25
			y1#=-.49
			y2#=.49
		Case 2
			x1#=.25
			x2#=-.25
			y1#=.49
			y2#=.49
		Case 3
			x1#=-.25
			x2#=-.25
			y1#=.49
			y2#=-.49
		End Select
		AddVertex (mainSurface,x1,.101,y1,subtype*0.25+.01,.01)
		AddVertex (mainSurface,x2,.101,y2,subtype*0.25+.24,.01)
		AddVertex (mainSurface,x1,-.4,y1,subtype*0.25+.01,.24)
		AddVertex (mainSurface,x2,-.4,y2,subtype*0.25+.24,.24)
		AddTriangle (mainSurface,4+i*4,5+i*4,6+i*4)
		AddTriangle (mainSurface,5+i*4,7+i*4,6+i*4)
	Next
	UpdateNormals mainEntity
	; EntityTexture mainEntity,GateTexture

	colorEntity=CreateMesh()
	colorSurface=CreateSurface(colorEntity)
	NameEntity colorEntity,"FlipBridgeColor"
	AddVertex (colorSurface,-.20,.1011,.45,0,0);(tex Mod 8)*0.125+.01,(tex/8)*0.125+.51)
	AddVertex (colorSurface,-.10,.1011,.45,.5,0);(tex Mod 8)*0.125+.115,(tex/8)*0.125+.51)
	AddVertex (colorSurface,-.20,.1011,-.45,0,2);(tex Mod 8)*0.125+.01,(tex/8)*0.125+.51+.115)
	AddVertex (colorSurface,-.10,.1011,-.45,.5,2);(tex Mod 8)*0.125+.115,(tex/8)*0.125+.51+.115)
	AddTriangle (colorSurface,0,1,2)
	AddTriangle (colorSurface,1,3,2)
	AddVertex (colorSurface,.10,.1011,.45,.5,0);(tex Mod 8)*0.125+.01,(tex/8)*0.125+.51)
	AddVertex (colorSurface,.20,.1011,.45,1,0);(tex Mod 8)*0.125+.115,(tex/8)*0.125+.51)
	AddVertex (colorSurface,.10,.1011,-.45,.5,2);(tex Mod 8)*0.125+.01,(tex/8)*0.125+.51+.115)
	AddVertex (colorSurface,.20,.1011,-.45,1,2);(tex Mod 8)*0.125+.115,(tex/8)*0.125+.51+.115)
	AddTriangle (colorSurface,4,5,6)
	AddTriangle (colorSurface,5,7,6)
	UpdateNormals colorEntity
	; EntityTexture colorEntity,GetColorTexture(tex)
	EntityParent colorEntity,mainEntity

	cylinderEntity=CreateCylinder(32,True)
	NameEntity cylinderEntity,"FlipBridgeCylinder"
	ScaleMesh cylinderEntity,.35,.35,.35
	PositionMesh cylinderEntity,0,-.241,0
	; EntityTexture cylinderEntity,CageTexture
	EntityParent cylinderEntity,mainEntity

	SizeFlipBridgeMesh(mainEntity,active,range)
	TextureFlipBridgeMesh(mainEntity,tex)

	Return mainEntity
End Function

Function TextureFlipBridgeMesh(flipbridge_handle,tex=0)
	EntityTexture flipbridge_handle,GateTexture
	EntityTexture FindChild(flipbridge_handle,"FlipBridgeColor"),GetColorTexture(tex)
	EntityTexture FindChild(flipbridge_handle,"FlipBridgeCylinder"),CageTexture
End Function

Function RedoFlipBridgeTexture(this.woi)
	; return if invalid model name
	If this\ModelName$<>"!FlipBridge"
		Return
	EndIf
	TextureFlipBridgeMesh(this\Entity,(this\ID-500)/5)
End Function

Function SizeFlipBridgeMesh(flipbridge_handle,active=0,range=3)
	mainSurface=GetSurface(flipbridge_handle,1)
	VertexCoords mainSurface,0,-.25,.1,.49+(range-.2)*active/1001.0
	VertexCoords mainSurface,1,.25,.1,.49+(range-.2)*active/1001.0
	VertexCoords mainSurface,2,-.25,.1,-.49-(range-.2)*active/1001.0
	VertexCoords mainSurface,3,.25,.1,-.49-(range-.2)*active/1001.0
	For i=0 To 3
		Select i
		Case 0
			x1#=-.25
			x2#=.25
			y1#=-.49-(range-.2)*active/1001.0
			y2#=-.49-(range-.2)*active/1001.0
		Case 1
			x1#=.25
			x2#=.25
			y1#=-.49-(range-.2)*active/1001.0
			y2#=.49+(range-.2)*active/1001.0
		Case 2
			x1#=.25
			x2#=-.25
			y1#=.49+(range-.2)*active/1001.0
			y2#=.49+(range-.2)*active/1001.0
		Case 3
			x1#=-.25
			x2#=-.25
			y1#=.49+(range-.2)*active/1001.0
			y2#=-.49-(range-.2)*active/1001.0
		End Select
		VertexCoords mainSurface,4+i*4,x1,.101,y1
		VertexCoords mainSurface,5+i*4,x2,.101,y2
		VertexCoords mainSurface,6+i*4,x1,-.4,y1
		VertexCoords mainSurface,7+i*4,x2,-.4,y2
	Next
	UpdateNormals flipbridge_handle
	colorSurface=GetSurface(FindChild(flipbridge_handle,"FlipBridgeColor"),1)
	VertexCoords colorSurface,0,-.20,.1011,.45+(range-.2)*active/1001.0
	VertexCoords colorSurface,1,-.10,.1011,.45+(range-.2)*active/1001.0
	VertexCoords colorSurface,2,-.20,.1011,-.45-(range-.2)*active/1001.0
	VertexCoords colorSurface,3,-.10,.1011,-.45-(range-.2)*active/1001.0
	VertexCoords colorSurface,4,.10,.1011,.45+(range-.2)*active/1001.0
	VertexCoords colorSurface,5,.20,.1011,.45+(range-.2)*active/1001.0
	VertexCoords colorSurface,6,.10,.1011,-.45-(range-.2)*active/1001.0
	VertexCoords colorSurface,7,.20,.1011,-.45-(range-.2)*active/1001.0
	UpdateNormals FindChild(flipbridge_handle,"FlipBridgeColor")
End Function

Function CreateTransporterMesh(tex,subtype=3,turn=1)
	mainEntity=CreateMesh()
	mainSurface=CreateSurface(mainEntity)
	; NameEntity mainEntity,"TransporterMain"
	colorEntity=CreateMesh()
	colorSurface=CreateSurface(colorEntity)
	NameEntity colorEntity,"TransporterColour"
	; Top 
	AddVertex (mainSurface,-.499,0.001,.499,subtype*0.25+.01,.26)
	AddVertex (mainSurface,.499,0.001,.499,subtype*0.25+.24,.26)
	AddVertex (mainSurface,-.499,0.001,-.499,subtype*0.25+.01,.49)
	AddVertex (mainSurface,.499,0.001,-.499,subtype*0.25+.24,.49)
	AddTriangle (mainSurface,0,1,2)
	AddTriangle (mainSurface,1,3,2)
	AddVertex (colorSurface,-.25,0.002,.25,0,0);(tex Mod 8)*0.125+.01,(tex/8)*0.125+.51)
	AddVertex (colorSurface,.25,0.002,.25,1,0);(tex Mod 8)*0.125+.115,(tex/8)*0.125+.51)
	AddVertex (colorSurface,-.25,0.002,-.25,0,1);(tex Mod 8)*0.125+.01,(tex/8)*0.125+.51+.115)
	AddVertex (colorSurface,.25,0.002,-.25,1,1);(tex Mod 8)*0.125+.115,(tex/8)*0.125+.51+.115)
	AddTriangle (colorSurface,0,1,2)
	AddTriangle (colorSurface,1,3,2)
	; Sides
	For i=0 To 3
		Select i
		Case 0
			x1#=-.498
			x2#=.498
			y1#=-.498
			y2#=-.498
		Case 1
			x1#=.498
			x2#=.498
			y1#=-.498
			y2#=.498
		Case 2
			x1#=.498
			x2#=-.498
			y1#=.498
			y2#=.498
		Case 3
			x1#=-.498
			x2#=-.498
			y1#=.498
			y2#=-.498
		End Select
		AddVertex (mainSurface,x1,0,y1,subtype*0.25+.01,.01)
		AddVertex (mainSurface,x2,0,y2,subtype*0.25+.24,.01)
		AddVertex (mainSurface,x1,-.3,y1,subtype*0.25+.01,.24)
		AddVertex (mainSurface,x2,-.3,y2,subtype*0.25+.24,.24)
		AddTriangle (mainSurface,4+i*4,5+i*4,6+i*4)
		AddTriangle (mainSurface,5+i*4,7+i*4,6+i*4)
	Next
	AddVertex (mainSurface,-.499,0.002,.499,turn*0.25+.01,.26)
	AddVertex (mainSurface,.499,0.002,.499,turn*0.25+.24,.26)
	AddVertex (mainSurface,-.499,0.002,-.499,turn*0.25+.01,.49)
	AddVertex (mainSurface,.499,0.002,-.499,turn*0.25+.24,.49)
	AddTriangle (mainSurface,20,21,22)
	AddTriangle (mainSurface,21,23,22)
	UpdateNormals mainEntity
	UpdateNormals colorEntity
	EntityParent colorEntity,mainEntity
	TextureTransporterMesh(mainEntity,tex,turn)
	Return mainEntity
End Function

Function TextureTransporterMesh(transporter_handle,tex=0,turn=1)
	surface=GetSurface(transporter_handle,1)
	VertexTexCoords surface,20,turn*0.25+.01,.26+.5
	VertexTexCoords surface,21,turn*0.25+.24,.26+.5
	VertexTexCoords surface,22,turn*0.25+.01,.49+.5
	VertexTexCoords surface,23,turn*0.25+.24,.49+.5
	UpdateNormals transporter_handle
	EntityTexture transporter_handle,TransporterTexture
	EntityTexture FindChild(transporter_handle,"TransporterColour"),GetColorTexture(tex)
End Function

Function RedoTransporterTexture(this.woi)
	; return if invalid model name
	If this\ModelName$<>"!Transporter" And this\ModelName$<>"!Conveyor"
		Return
	EndIf
	If (this\ObjType=45 Or this\ObjType=46) And this\ObjData[4]=4 ; cloud
		TextureCloudMesh(this\Entity,(this\ID-500)/5)
	Else
		If this\ObjType=45 Or this\ObjType=46
			; disable turn for conveyors
			If this\ObjData[4]<3
				TextureTransporterMesh(this\Entity,(this\ID-500)/5,0)
			Else
				TextureTransporterMesh(this\Entity,(this\ID-500)/5,1)
			EndIf
		Else
			TextureTransporterMesh(this\Entity,(this\ID-500)/5,this\ObjData[3]+1)
		EndIf
	EndIf
End Function

Function CreateCloudMesh(col,bigr#=1.1)	; Q - this is for star trails from PoTZ
	Entity=CreateMesh()
	Surface=CreateSurface(Entity)
	r#=.4*bigr
	angle=0;Rand(0,359)
	AddVertex (surface,r*Cos(angle+135),0.01,r*Sin(angle+135),0,0)
	AddVertex (surface,r*Cos(angle+45),0.01,r*Sin(angle+45),1,0)
	AddVertex (surface,r*Cos(angle+225),0.01,r*Sin(angle+225),0,1)
	AddVertex (surface,r*Cos(angle+315),0.01,r*Sin(angle+315),1,1)
	AddTriangle (surface,0,1,2)
	AddTriangle (surface,1,3,2)
	r#=.3*bigr
	angle=Rand(0,359)
	AddVertex (surface,r*Cos(angle+135),0.01,r*Sin(angle+135),0,0)
	AddVertex (surface,r*Cos(angle+45),0.01,r*Sin(angle+45),1,0)
	AddVertex (surface,r*Cos(angle+225),0.01,r*Sin(angle+225),0,1)
	AddVertex (surface,r*Cos(angle+315),0.01,r*Sin(angle+315),1,1)
	AddTriangle (surface,4,5,6)
	AddTriangle (surface,5,7,6)
	r#=.2*bigr
	angle=Rand(0,359)
	AddVertex (surface,r*Cos(angle+135),0.01,r*Sin(angle+135),0,0)
	AddVertex (surface,r*Cos(angle+45),0.01,r*Sin(angle+45),1,0)
	AddVertex (surface,r*Cos(angle+225),0.01,r*Sin(angle+225),0,1)
	AddVertex (surface,r*Cos(angle+315),0.01,r*Sin(angle+315),1,1)
	AddTriangle (surface,8,9,10)
	AddTriangle (surface,9,11,10)
	UpdateNormals Entity
	EntityAlpha Entity,.8
	EntityFX Entity,1
	TextureCloudMesh(Entity,col)
	Return Entity
End Function

Function TextureCloudMesh(cloud_handle,tex=0)
	EntityTexture(cloud_handle,CloudTexture,0,0)
	EntityTexture(cloud_handle,GetColorTexture(tex),0,1)
End Function

Function CreateKeyMesh(col=0)
	entity=CopyMesh(KeyMesh)
	;adjust the colour
	For i=0 To CountVertices (GetSurface(entity,1))-1
		If VertexU(GetSurface(entity,1),i)>0.124 
			u#=0.124
		Else
			u#=VertexU(GetSurface(entity,1),i)
		EndIf
		If col=8
			VertexTexCoords GetSurface(entity,1),i,8.0*VertexV(GetSurface(entity,1),i,0),8.0*(u*0.7+0.5)
		Else
			VertexTexCoords GetSurface(entity,1),i,8.0*u,8.0*(VertexV(GetSurface(entity,1),i,0)+0.5)
		EndIf
	Next
	; ;EntityFX Entity,2
	UpdateNormals entity
	TextureKeyMesh(entity,col)
	Return entity
End Function

Function TextureKeyMesh(key_handle,col=0)
	EntityTexture key_handle,GetColorTexture(col);ButtonTexture
End Function

Function CreateKeyCardMesh(col=0)
	; tex=24+col
	mainEntity=CreateMesh()
	surface=CreateSurface(mainEntity)
	AddVertex (surface,-.4,.4,-.021,0,0);(tex Mod 8)*0.125+0.000,Floor((tex Mod 64)/8)*0.125+0.000)
	AddVertex (surface,.4,.4,-.021,1,0);(tex Mod 8)*0.125+0.125,Floor((tex Mod 64)/8)*0.125+0.000)
	AddVertex (surface,-.4,-.4,-.021,0,1);(tex Mod 8)*0.125+0.000,Floor((tex Mod 64)/8)*0.125+0.125)
	AddVertex (surface,.4,-.4,0-.021,1,1);(tex Mod 8)*0.125+0.125,Floor((tex Mod 64)/8)*0.125+0.125)
	AddVertex (surface,-.4,.4,.021,0,0);(tex Mod 8)*0.125+0.000,Floor((tex Mod 64)/8)*0.125+0.000)
	AddVertex (surface,.4,.4,.021,1,0);(tex Mod 8)*0.125+0.125,Floor((tex Mod 64)/8)*0.125+0.000)
	AddVertex (surface,-.4,-.4,.021,0,1);(tex Mod 8)*0.125+0.000,Floor((tex Mod 64)/8)*0.125+0.125)
	AddVertex (surface,.4,-.4,.021,1,1);(tex Mod 8)*0.125+0.125,Floor((tex Mod 64)/8)*0.125+0.125)
	AddTriangle(surface,0,1,2)
	AddTriangle(surface,1,3,2)
	AddTriangle(surface,5,4,6)
	AddTriangle(surface,5,6,7)
	UpdateNormals mainEntity
	; EntityTexture mainEntity,ButtonTexture
	overlayEntity = CopyEntity(mainEntity,mainEntity)
	NameEntity(overlayEntity,"KeyCardOverlay")
	TextureKeyCardMesh(mainEntity,col)
	Return mainEntity
End Function

Function CreateOldKeyCardMesh(col)
	tex=24+col
	Entity=CreateMesh()
	surface=CreateSurface(entity)	
	AddVertex (surface,-.4,.4,-.021,(tex Mod 8)*0.125+0.000,Floor((tex Mod 64)/8)*0.125+0.000)
	AddVertex (surface,.4,.4,-.0210,(tex Mod 8)*0.125+0.125,Floor((tex Mod 64)/8)*0.125+0.000)
	AddVertex (surface,-.4,-.4,-.0210,(tex Mod 8)*0.125+0.000,Floor((tex Mod 64)/8)*0.125+0.125)
	AddVertex (surface,.4,-.4,0-.021,(tex Mod 8)*0.125+0.125,Floor((tex Mod 64)/8)*0.125+0.125)	
	AddVertex (surface,-.4,.4,.0210,(tex Mod 8)*0.125+0.000,Floor((tex Mod 64)/8)*0.125+0.000)
	AddVertex (surface,.4,.4,.0210,(tex Mod 8)*0.125+0.125,Floor((tex Mod 64)/8)*0.125+0.000)
	AddVertex (surface,-.4,-.4,.0210,(tex Mod 8)*0.125+0.000,Floor((tex Mod 64)/8)*0.125+0.125)
	AddVertex (surface,.4,-.4,.0210,(tex Mod 8)*0.125+0.125,Floor((tex Mod 64)/8)*0.125+0.125)
	AddTriangle(surface,0,1,2)
	AddTriangle(surface,1,3,2)	
	AddTriangle(surface,5,4,6)
	AddTriangle(surface,5,6,7)		
	UpdateNormals Entity
	EntityTexture Entity,ButtonTexture
	Return Entity
End Function

Function TextureKeyCardMesh(keycard_handle,col=0)
	EntityTexture(keycard_handle,KeyCardTexture(0),0,0)
	EntityTexture(keycard_handle,GetColorTexture(col),0,1)
	EntityTexture FindChild(keycard_handle,"KeyCardOverlay"),KeyCardTexture(1)
End Function

Function CreateTeleporterMesh(tex=0,objtex$="!None",this.woi)
	entity=CreateCylinder(16,False)
	PositionMesh entity,0,1,0
	ScaleMesh entity,.4,2,.4
	If tex<0 Or tex>15
		tex=0
	EndIf
	EntityBlend entity,3 ; This actually makes it almost impossible to see the difference between colours on a bright leveltex!
	EntityFX entity,2+32 ; to reconsider: +1 for full-bright
	surface=GetSurface(entity,1)
	For i=0 To 16
		VertexColor surface,i*2,0,0,0,0
		VertexColor surface,i*2+1,255,255,255,1
	Next
	EntityAlpha entity,.6 ; changed to vertex alpha
	
	If (objtex$="!None" Or this\ObjData[3]=1)  
		TextureTeleporterMesh(entity,tex,this\ObjData[3])
	EndIf

	Return entity
End Function

Function TextureTeleporterMesh(teleporter_handle,tex,teltexflag=0)
	uvScaleChild=FindChild(teleporter_handle,"UVScale")
	If uvScaleChild=0 ; used regular scale before
		If Abs(tex Mod 16)>7 ; should use 4x scale after
			uvScaleChild=CreateMesh()
			NameEntity uvScaleChild,"UVScale"
			EntityParent uvScaleChild,teleporter_handle
			
			For i=1 To CountSurfaces(teleporter_handle)
				surface=GetSurface(teleporter_handle,i)
				For j=0 To CountVertices(surface)-1
					VertexTexCoords surface,j,4.0*VertexU(surface,j),4.0*VertexV(surface,j)
				Next
			Next
			UpdateNormals teleporter_handle
		EndIf
	Else ; used 4x scale before
		If Abs(tex Mod 16)<8 ; should use regular scale after
			FreeEntity uvScaleChild
			
			For i=1 To CountSurfaces(teleporter_handle)
				surface=GetSurface(teleporter_handle,i)
				For j=0 To CountVertices(surface)-1
					VertexTexCoords surface,j,0.25*VertexU(surface,j),0.25*VertexV(surface,j)
				Next
			Next
			UpdateNormals teleporter_handle
		EndIf
	EndIf
	
	ApplyTeleporterTexture(teleporter_handle,tex,teltexflag)
		
End Function

Function ApplyTeleporterTexture(teleporter_handle,tex=0,teltexflag=0)
	starChild=FindChild(teleporter_handle,"TeleporterStars")
	If starChild<>0
		FreeEntity starChild
	EndIf
	starChild = CopyEntity(teleporter_handle)
	NameEntity starChild,"TeleporterStars"
	EntityParent starChild,teleporter_handle


	EntityTexture teleporter_handle,GetColorTexture(tex,32)
			

	If teltexflag=2 
		EntityTexture starChild,GetColorTexture(15,32)
	Else
		EntityTexture starChild,TeleporterTexture
	EndIf
	;EntityBlend starChild,1
	EntityBlend starChild,3
	;EndIf
	
End Function

Function CreateWaterFallMesh(tex)
	Entity=CreateMesh()
	surface=CreateSurface(Entity)
	AddVertex surface,-.5,0.81,-.53,0,0
	AddVertex surface,.5,0.81,-.53,1,0
	AddVertex surface,-.5,-0.21,-.53,0,1
	AddVertex surface,.5,-0.21,-.53,1,1
	AddVertex surface,-.5,0.31,-.53,0,1
	AddVertex surface,.5,0.31,-.53,1,1
	AddVertex surface,-.5,0.31,-.53,0,0
	AddVertex surface,.5,0.31,-.53,1,0
	AddTriangle (surface,0,1,2)
	AddTriangle (surface,1,3,2)
	; AddTriangle (surface,0,1,4) ; uncommented in vanilla editor
	; AddTriangle (surface,1,5,4) ; uncommented in vanilla editor
	; AddTriangle (surface,6,7,2) ; uncommented in vanilla editor
	; AddTriangle (surface,7,3,2) ; uncommented in vanilla editor
	AddTriangle (surface,1,0,3)
	AddTriangle (surface,0,2,3)
	EntityTexture Entity,WaterFallTexture(tex)
	EntityAlpha Entity,.5 ; vanilla editor: .7
	UpdateNormals Entity
	Return Entity
End Function

Function CreateCustomItemMesh(tex)
	entity=CreateMesh()
	surface=CreateSurface(entity)
	AddVertex (surface,-.4,.4,-.0955,(tex Mod 8)*0.125+0.013,Floor((tex Mod 64)/8)*0.125+0.013)
	AddVertex (surface,.4,.4,-.0955,(tex Mod 8)*0.125+0.1135,Floor((tex Mod 64)/8)*0.125+0.013)
	AddVertex (surface,-.4,-.4,-.0955,(tex Mod 8)*0.125+0.013,Floor((tex Mod 64)/8)*0.125+0.0945)
	AddVertex (surface,.4,-.4,0-.0955,(tex Mod 8)*0.125+0.1135,Floor((tex Mod 64)/8)*0.125+0.0945)
	AddVertex (surface,-.4,.4,.0955,(tex Mod 8)*0.125+0.013,Floor((tex Mod 64)/8)*0.125+0.013)
	AddVertex (surface,.4,.4,.0955,(tex Mod 8)*0.125+0.1135,Floor((tex Mod 64)/8)*0.125+0.013)
	AddVertex (surface,-.4,-.4,.0955,(tex Mod 8)*0.125+0.013,Floor((tex Mod 64)/8)*0.125+0.0945)
	AddVertex (surface,.4,-.4,.0955,(tex Mod 8)*0.125+0.1135,Floor((tex Mod 64)/8)*0.125+0.0945)
	AddTriangle(surface,0,1,2)
	AddTriangle(surface,1,3,2)
	AddTriangle(surface,5,4,6)
	AddTriangle(surface,5,6,7)
	AddVertex (surface,-.45,.45,-.095,7*0.125+0.013,7*0.125+0.013)
	AddVertex (surface,.45,.45,-.095,7*0.125+0.1135,7*0.125+0.013)
	AddVertex (surface,-.45,-.45,-.095,7*0.125+0.013,7*0.125+0.0945)
	AddVertex (surface,.45,-.45,0-.095,7*0.125+0.1135,7*0.125+0.0945)
	AddVertex (surface,-.45,.45,.095,7*0.125+0.013,7*0.125+0.013)
	AddVertex (surface,.45,.45,.095,7*0.125+0.1135,7*0.125+0.013)
	AddVertex (surface,-.45,-.45,.095,7*0.125+0.013,7*0.125+0.0945)
	AddVertex (surface,.45,-.45,.095,7*0.125+0.1135,7*0.125+0.0945)
	AddTriangle(surface,8,9,10)
	AddTriangle(surface,9,11,10)
	AddTriangle(surface,13,12,14)
	AddTriangle(surface,13,14,15)
	AddTriangle(surface,12,13,9)
	AddTriangle(surface,8,12,9)
	AddTriangle(surface,11,15,14)
	AddTriangle(surface,11,14,10)
	AddTriangle(surface,12,8,14)
	AddTriangle(surface,8,10,14)
	AddTriangle(surface,9,13,15)
	AddTriangle(surface,9,15,11)
	UpdateNormals entity
	EntityTexture entity,IconTextureCustom
	; RotateMesh Entity,90,0,0
	; PositionMesh Entity,0,.3,0
	Return entity
End Function

Function CreatePickUpItemMesh(tex)
	entity=CreateMesh()
	surface=CreateSurface(entity)
	AddVertex (surface,0,.1,0,.9375,.9375)
	R#=.3
	AddVertex (surface,R,.1,.5+R,.875,.875)
	AddVertex (surface,.5+R,.1,R,.875,1)
	AddTriangle (surface,0,1,2)
	AddVertex (surface,.5+R,.1,-R,1,1)
	AddTriangle (surface,0,2,3)
	AddVertex (surface,+R,.1,-.5-R,1,.875)
	AddTriangle (surface,0,3,4)
	AddVertex (surface,-R,.1,-.5-R,.875,.875)
	AddTriangle (surface,0,4,5)
	AddVertex (surface,-.5-R,.1,-R,1,.875)
	AddTriangle (surface,0,5,6)
	AddVertex (surface,-.5-R,.1,R,1,1)
	AddTriangle (surface,0,6,7)
	AddVertex (surface,-R,.1,.5+R,.875,1)
	AddTriangle (surface,0,7,8)
	AddTriangle (surface,0,8,1)
	AddVertex (surface,-.5,.2,.5,(tex Mod 8)*0.125+0.013,Floor((tex Mod 64)/8)*0.125+0.013)
	AddVertex (surface,.5,.2,.5,(tex Mod 8)*0.125+0.1135,Floor((tex Mod 64)/8)*0.125+0.013)
	AddVertex (surface,-.5,.2,-.5,(tex Mod 8)*0.125+0.013,Floor((tex Mod 64)/8)*0.125+0.0945)
	AddVertex (surface,.5,.2,-.5,(tex Mod 8)*0.125+0.1135,Floor((tex Mod 64)/8)*0.125+0.0945)
	AddTriangle (surface,9,10,11)
	AddTriangle (surface,10,12,11)
	AddVertex(surface,0,1.5,0,.9375,.9375)
	AddTriangle (surface,13,1,2)
	AddTriangle (surface,13,2,3)
	AddTriangle (surface,13,3,4)
	AddTriangle (surface,13,4,5)
	AddTriangle (surface,13,5,6)
	AddTriangle (surface,13,6,7)
	AddTriangle (surface,13,7,8)
	AddTriangle (surface,13,8,1)
	For j=0 To 8
		VertexColor  surface,j,255,255,255,.5
	Next
	VertexColor  surface,13,255,255,255,.5
	EntityFX entity,32+2
	ScaleMesh entity,.5,.5,.5
	UpdateNormals entity
	; EntityTexture Entity,IconTextureStandard
	Return entity
End Function

Function CreateButtonMesh(btype,col1,col2,col3,col4)
	; texture is the available "colour" from 0-15
	; If btype=11 Or btype>=32
		; ; npc commands or invisible
		; GameObject(i)\Entity=CreatePivot()
		; Return
	; EndIf
	; ; expired adventurestart button?
	; If btype=13
		; If AdventureCompleted(GameObject(i)\ObjData[0])=1 
			; GameObject(i)\SubType=14
			; btype=14
		; EndIf
	; EndIf
	btype=btype Mod 32
	If btype=15
		btype=11
	Else If btype>=5 And btype<10 ; Switchers use just two colours
		col3=col1
		col4=col2
	Else If btype=16 Or btype=17; Rotators use just one colour
		col2=col1
		col3=col1
		col4=col1
	EndIf
	entity=CreateMesh()
	surface=CreateSurface(entity)
	; first, the outline shape
	AddVertex (surface,-.45,0.01,.45,(btype Mod 8)*0.125,(btype/8)*0.125)
	AddVertex (surface,.45,0.01,.45,(btype Mod 8)*0.125+0.125,(btype/8)*0.125)
	AddVertex (surface,-.45,0.01,-.45,(btype Mod 8)*0.125,(btype/8)*0.125+0.125)
	AddVertex (surface,.45,0.01,-.45,(btype Mod 8)*0.125+0.125,(btype/8)*0.125+0.125)
	AddTriangle (surface,0,1,2)
	AddTriangle (surface,1,3,2)
	If btype<10 Or btype>15
		; now the four colours - the placement of these depend on the btype shape
		Select btype
			Case 0,5 ; square
				radius#=.4
				alt=True
			Case 1,6,16,17 ; circle
				radius#=.323
				alt=True
			Case 2,3,7,8 ; diamond
				radius#=.4
				alt=False
			Case 4,9 ; star
				radius#=.323
				alt=True
		End Select
		nudge2#=.002 ; .01
		AddVertex (surface,-radius,0.005,radius,(col1 Mod 8)*0.125+.01,(col1/8)*0.125+0.5+.01) ; 0.02
		AddVertex (surface,nudge2,0.005,radius,(col1 Mod 8)*0.125+0.125-.01,(col1/8)*0.125+.5+.01)
		AddVertex (surface,-radius,0.005,-nudge2,(col1 Mod 8)*0.125+.01,(col1/8)*0.125+0.5+.125-.01)
		AddVertex (surface,nudge2,0.005,-nudge2,(col1 Mod 8)*0.125+0.125-.01,(col1/8)*0.125+.5+.125-.01)
		If alt=True
			AddTriangle (surface,4,5,6)
			AddTriangle (surface,5,7,6)
		Else
			AddTriangle (surface,5,7,6)
		EndIf
		AddVertex (surface,-nudge2,0.005,radius,(col2 Mod 8)*0.125+.01,(col2/8)*0.125+0.5+.01)
		AddVertex (surface,radius,0.005,radius,(col2 Mod 8)*0.125+0.125-.01,(col2/8)*0.125+.5+.01)
		AddVertex (surface,-nudge2,0.005,-nudge2,(col2 Mod 8)*0.125+.01,(col2/8)*0.125+0.5+.125-.01)
		AddVertex (surface,radius,0.005,-nudge2,(col2 Mod 8)*0.125+0.125-.01,(col2/8)*0.125+.5+.125-.01)
		If alt=True
			AddTriangle (surface,8,9,10)
			AddTriangle (surface,9,11,10)
		Else
			AddTriangle (surface,8,11,10)
		EndIf
		AddVertex (surface,-radius,0.005,nudge2,(col3 Mod 8)*0.125+.01,(col3/8)*0.125+0.5+.01)
		AddVertex (surface,nudge2,0.005,nudge2,(col3 Mod 8)*0.125+0.125-.01,(col3/8)*0.125+.5+.01)
		AddVertex (surface,-radius,0.005,-radius,(col3 Mod 8)*0.125+.01,(col3/8)*0.125+0.5+.125-.01)
		AddVertex (surface,nudge2,0.005,-radius,(col3 Mod 8)*0.125+0.125-.01,(col3/8)*0.125+.5+.125-.01)
		If alt=True
			AddTriangle (surface,12,13,14)
			AddTriangle (surface,13,15,14)
		Else
			AddTriangle (surface,12,13,15)
		EndIf
		AddVertex (surface,-nudge2,0.005,nudge2,(col4 Mod 8)*0.125+.01,(col4/8)*0.125+0.5+.01)
		AddVertex (surface,radius,0.005,nudge2,(col4 Mod 8)*0.125+0.125-.01,(col4/8)*0.125+.5+.01)
		AddVertex (surface,-nudge2,0.005,-radius,(col4 Mod 8)*0.125+.01,(col4/8)*0.125+0.5+.125-.01)
		AddVertex (surface,radius,0.005,-radius,(col4 Mod 8)*0.125+0.125-.01,(col4/8)*0.125+.5+.125-.01)
		If alt=True
			AddTriangle (surface,16,17,18)
			AddTriangle (surface,17,19,18)
		Else
			AddTriangle (surface,16,17,18)
		EndIf
	EndIf
	If btype=10
		EntityFX entity,1
	EndIf
	UpdateNormals entity
	EntityTexture entity,ButtonTexture
	Return entity
End Function

Function TextureColourButtonMesh(button_handle,col1,col2,col3,col4)
	; unless invisible ; moved to ReDoButtonTexture()
	; If GameObject(i)\SubType>31
	; 	Return
	; EndIf
	nudge#=0.01
	surface=GetSurface(button_handle,1)
	; col1=GameObject(i)\ObjData[0]
	VertexTexCoords surface,4,(col1 Mod 8)*0.125+nudge,(col1/8)*0.125+0.5+nudge
	VertexTexCoords surface,5,(col1 Mod 8)*0.125+0.125-nudge,(col1/8)*0.125+.5+nudge
	VertexTexCoords surface,6,(col1 Mod 8)*0.125+nudge,(col1/8)*0.125+0.5+.125-nudge
	VertexTexCoords surface,7,(col1 Mod 8)*0.125+0.125-nudge,(col1/8)*0.125+.5+.125-nudge
	; col2=GameObject(i)\ObjData[1]
	VertexTexCoords surface,8,(col2 Mod 8)*0.125+nudge,(col2/8)*0.125+0.5+nudge
	VertexTexCoords surface,9,(col2 Mod 8)*0.125+0.125-nudge,(col2/8)*0.125+.5+nudge
	VertexTexCoords surface,10,(col2 Mod 8)*0.125+nudge,(col2/8)*0.125+0.5+.125-nudge
	VertexTexCoords surface,11,(col2 Mod 8)*0.125+0.125-nudge,(col2/8)*0.125+.5+.125-nudge
	; If GameObject(i)\SubType>=5 And GameObject(i)\SubType<10
		; col3=GameObject(i)\ObjData[0]
	; Else
		; col3=GameObject(i)\ObjData[2]
	; EndIf
	VertexTexCoords surface,12,(col3 Mod 8)*0.125+nudge,(col3/8)*0.125+0.5+nudge
	VertexTexCoords surface,13,(col3 Mod 8)*0.125+0.125-nudge,(col3/8)*0.125+.5+nudge
	VertexTexCoords surface,14,(col3 Mod 8)*0.125+nudge,(col3/8)*0.125+0.5+.125-nudge
	VertexTexCoords surface,15,(col3 Mod 8)*0.125+0.125-.02,(col3/8)*0.125+.5+.125-nudge
	; If GameObject(i)\SubType>=5 And GameObject(i)\SubType<10
		; col4=GameObject(i)\ObjData[1]
	; Else
		; col4=GameObject(i)\ObjData[3]
	; EndIf
	VertexTexCoords surface,16,(col4 Mod 8)*0.125+nudge,(col4/8)*0.125+0.5+nudge
	VertexTexCoords surface,17,(col4 Mod 8)*0.125+0.125-nudge,(col4/8)*0.125+.5+nudge
	VertexTexCoords surface,18,(col4 Mod 8)*0.125+nudge,(col4/8)*0.125+0.5+.125-nudge
	VertexTexCoords surface,19,(col4 Mod 8)*0.125+0.125-nudge,(col4/8)*0.125+.5+.125-nudge
	UpdateNormals button_handle
End Function

Function TextureSpecialButtonMesh(button_handle,btype)
	surface=GetSurface(button_handle,1)
	VertexTexCoords surface,0,(btype Mod 8)*0.125,(btype/8)*0.125
	VertexTexCoords surface,1,(btype Mod 8)*0.125+0.125,(btype/8)*0.125
	VertexTexCoords surface,2,(btype Mod 8)*0.125,(btype/8)*0.125+0.125
	VertexTexCoords surface,3,(btype Mod 8)*0.125+0.125,(btype/8)*0.125+0.125
	UpdateNormals button_handle
End Function

Function ReDoButtonTexture(this.woi)
	; return if invalid model name
	If this\ModelName$<>"!Button"
		Return
	EndIf
	; unless invisible
	If this\SubType>31
		Return
	EndIf
	If this\SubType<10 or this\SubType>15 ; coloured button
		col1=this\ObjData[0]
		col2=this\ObjData[1]
		If this\SubType>=5 And this\SubType<10
			col3=this\ObjData[0]
		Else
			col3=this\ObjData[2]
		EndIf
		If this\SubType>=5 And this\SubType<10
			col4=this\ObjData[1]
		Else
			col4=this\ObjData[3]
		EndIf
		TextureColourButtonMesh(this\Entity,col1,col2,col3,col4)
	Else
		TextureSpecialButtonMesh(this\Entity,this\SubType)
	EndIf
End Function

Function CreatePlantFloatMesh()
	entity=CreateCylinder(9,True)
	;For i=1 To CountVertices (GetSurface(entity,1))-1
	;	VertexCoords GetSurface(entity,1),i,VertexX(GetSurface(entity,1),i)+Rnd(-.1,.1),VertexY(GetSurface(entity,1),i),VertexZ(GetSurface(entity,1),i)+Rnd(-.1,.1)
	;Next
	ScaleMesh entity,.45,.05,.45
	PositionMesh entity,0,-.1,0
	EntityAlpha entity,.7
	EntityBlend entity,3
	EntityColor entity,0,255,0
	Return entity
End Function

Function CreateRetroLaserGateMesh(col)
	; create the mesh if laser gate
	entity=CreateMesh() 
	cylinder=CreateCylinder(6,False) ; an individual Cylinder
	ScaleMesh cylinder,0.05,0.5,0.05
	RotateMesh cylinder,0,0,90
	PositionMesh cylinder,0,.25,0.0
	AddMesh cylinder,entity
	PositionMesh cylinder,0,-.375,.2165
	AddMesh cylinder,entity
	PositionMesh cylinder,0,0,-.433
	AddMesh cylinder,entity
	FreeEntity cylinder
	; EntityAlpha Entity,0.5 ; Q - commented out because this doesn't seem to work, actual alpha fix is now under ControlRetroLaserGate
	; Select col
		; Case 0
			; EntityColor entity,255,0,0
		; Case 1
			; EntityColor entity,255,128,0
		; Case 2
			; EntityColor entity,255,255,0
		; Case 3
			; EntityColor entity,0,255,0
		; Case 4
			; EntityColor entity,0,255,255
		; Case 5
			; EntityColor entity,0,0,255
		; Default
			; EntityColor entity,255,0,255
	; End Select
	TextureLaserGateMesh(entity,col)
	Return entity
End Function

Function TextureLaserGateMesh(lasergate_handle,col)
	EntityTexture lasergate_handle,GetColorTexture(col)
End Function

Function CreatePushbotMesh(tex,dir)
	mainEntity=CreateMesh()
	mainSurface=CreateSurface(mainEntity)
	If dir=2
		; special for turn-around
		dir=0
		; Front
		AddVertex (mainSurface,-.4,0,.4,0,.25+.25*dir)
		AddVertex (mainSurface,+.4,0,.4,0,0+.25*dir)
		AddVertex (mainSurface,-.2,.3,.2,.25,.20+.25*dir)
		AddVertex (mainSurface,+.2,.3,.2,.25,.05+.25*dir)
		AddTriangle (mainSurface,0,1,2)
		AddTriangle (mainSurface,1,3,2)
		; Top
		AddVertex (mainSurface,-.4,.4,-.4,.5,.20+.25*dir)
		AddVertex (mainSurface,+.4,.4,-.4,.5,.05+.25*dir)
		AddTriangle (mainSurface,2,3,4)
		AddTriangle (mainSurface,3,5,4)
		; Back
		AddVertex (mainSurface,-.45,0,-.45,.75,.25+.25*dir)
		AddVertex (mainSurface,+.45,0,-.45,.75,0+.25*dir)
		AddTriangle (mainSurface,4,5,6)
		AddTriangle (mainSurface,5,7,6)
		; Left
		AddVertex (mainSurface,-.4,0,.4,.75,.25+.25*dir)
		AddVertex (mainSurface,-.45,0,-.45,.75,0+.25*dir)
		AddVertex (mainSurface,-.2,.3,.2,1,.25+.25*dir)
		AddVertex (mainSurface,-.4,.4,-.4,1,0+.25*dir)
		AddTriangle (mainSurface,10,9,8)
		AddTriangle (mainSurface,10,11,9)
		; Right
		AddVertex (mainSurface,.4,0,.4,.75,.25+.25*dir)
		AddVertex (mainSurface,.45,0,-.45,.75,0+.25*dir)
		AddVertex (mainSurface,.2,.3,.2,1,.25+.25*dir)
		AddVertex (mainSurface,.4,.4,-.4,1,0+.25*dir)
		AddTriangle (mainSurface,12,13,14)
		AddTriangle (mainSurface,13,15,14)
	Else
		dir=1-dir
		; Front
		AddVertex (mainSurface,-.4,0,.4,0,.25+.25*dir)
		AddVertex (mainSurface,+.4,0,.4,0,0+.25*dir)
		AddVertex (mainSurface,-.2,.3,.2,.25,.25+.25*dir)
		AddVertex (mainSurface,+.2,.3,.2,.25,0+.25*dir)
		AddTriangle (mainSurface,0,1,2)
		AddTriangle (mainSurface,1,3,2)
		; Top
		AddVertex (mainSurface,-.4,.4,-.4,.5,.25+.25*dir)
		AddVertex (mainSurface,+.4,.4,-.4,.5,0+.25*dir)
		AddTriangle (mainSurface,2,3,4)
		AddTriangle (mainSurface,3,5,4)
		; Back
		AddVertex (mainSurface,-.45,0,-.45,.75,.25+.25*dir)
		AddVertex (mainSurface,+.45,0,-.45,.75,0+.25*dir)
		AddTriangle (mainSurface,4,5,6)
		AddTriangle (mainSurface,5,7,6)
		; Left
		AddVertex (mainSurface,-.4,0,.4,.75,.25+.25*dir)
		AddVertex (mainSurface,-.45,0,-.45,.75,0+.25*dir)
		AddVertex (mainSurface,-.2,.3,.2,1,.25+.25*dir)
		AddVertex (mainSurface,-.4,.4,-.4,1,0+.25*dir)
		AddTriangle (mainSurface,10,9,8)
		AddTriangle (mainSurface,10,11,9)
		; Right
		AddVertex (mainSurface,.4,0,.4,.75,.25+.25*dir)
		AddVertex (mainSurface,.45,0,-.45,.75,0+.25*dir)
		AddVertex (mainSurface,.2,.3,.2,1,.25+.25*dir)
		AddVertex (mainSurface,.4,.4,-.4,1,0+.25*dir)
		AddTriangle (mainSurface,12,13,14)
		AddTriangle (mainSurface,13,15,14)
	EndIf
	UpdateNormals mainEntity
	EntityTexture mainEntity,PushbotTexture
	; Col
	colorEntity=CreateMesh()
	NameEntity colorEntity,"PushbotColor"
	colorSurface=CreateSurface(colorEntity)
	AddVertex (colorSurface,-.05,.33,.05,.4,0)
	AddVertex (colorSurface,.05,.33,.05,.6,0)
	AddVertex (colorSurface,-.25,.4,-.35,0,1)
	AddVertex (colorSurface,.25,.4,-.35,1,1)
	AddTriangle (colorSurface,0,1,2)
	AddTriangle (colorSurface,1,3,2)
	UpdateNormals colorEntity
	EntityParent colorEntity,mainEntity
	TexturePushbotMesh(mainEntity,tex)
	Return mainEntity
End Function

Function TexturePushbotMesh(pushbot_handle,tex)
	EntityTexture FindChild(pushbot_handle,"PushbotColor"),GetColorTexture(tex)
End Function

Function RedoPushbotTexture(this.woi)
	; return if invalid model name
	If this\ModelName$<>"!Pushbot"
		Return
	EndIf
	TexturePushbotMesh(this\Entity,(this\ID-500)/5)
End Function

Function CreateSucTubeMesh(tex=3,col=0,active=1)
	active = active Mod 2
	; If (active Mod 2)=1
		; active=0
	; Else
		; active=1
	; EndIf
	mainEntity=CreateMesh()
	mainSurface=CreateSurface(mainEntity)
	nofsegments#=16
	i=0
	angle#=-(360.0/nofsegments)/2.0+i*(360.0/nofsegments)
	For i=0 To nofsegments-1
		angle#=-(360.0/nofsegments)/2.0+i*(360.0/nofsegments)
		AddVertex (mainSurface,1.5*Sin(angle),0.7+1.0*Cos(angle),-0.505,0.25*tex,107.0/512.0)
		AddVertex (mainSurface,1.5*Sin(angle),0.7+1.0*Cos(angle),+0.505,0.25*tex,88.0/512.0)
		AddVertex (mainSurface,1.5*Sin(angle+(360.0/nofsegments)),0.7+1.0*Cos(angle+(360.0/nofsegments)),-0.505,0.25*tex+0.25,107.0/512.0)
		AddVertex (mainSurface,1.5*Sin(angle+(360.0/nofsegments)),0.7+1.0*Cos(angle+(360.0/nofsegments)),+0.505,0.25*tex+0.25,88.0/512.0)
		AddTriangle(mainSurface,i*4+0,i*4+1,i*4+2)
		AddTriangle(mainSurface,i*4+1,i*4+3,i*4+2)
		AddTriangle(mainSurface,i*4+2,i*4+1,i*4+0)
		AddTriangle(mainSurface,i*4+2,i*4+3,i*4+1)
	Next
	UpdateNormals mainEntity
	EntityTexture mainEntity,GateTexture

	colorEntity=CreateMesh()
	NameEntity colorEntity,"SucTubeColor"
	colorSurface=CreateSurface(colorEntity)
	; top triangle (pointing north)
	;AddVertex (colorSurface,-0.3,1.71,-0.3,(col Mod 8)*0.125+.01,(col/8)*0.125+.51+.25*active)
	;AddVertex (colorSurface,+0.3,1.71,-0.3,(col Mod 8)*0.125+.115,(col/8)*0.125+.51+.25*active)
	;AddVertex (colorSurface,0,1.71,+0.3,(col Mod 8)*0.125+.01,(col/8)*0.125+.51+.115+.25*active)
	AddVertex (colorSurface,-0.3,1.71,-0.3,0,0) ; bottom/left
	AddVertex (colorSurface,+0.3,1.71,-0.3,1,0) ; bottom/right
	AddVertex (colorSurface,0,1.71,+0.3,0.5,1) ; top/middle
	AddTriangle (colorSurface,0,2,1)
	; point arrow
	If dir=0
		VertexCoords colorSurface,0,-0.3,1.71,-0.3
		VertexCoords colorSurface,1,+0.3,1.71,-0.3
		VertexCoords colorSurface,2,0,1.71,+0.3
	Else
		VertexCoords colorSurface,0,-0.3,1.71,+0.3
		VertexCoords colorSurface,2,+0.3,1.71,+0.3
		VertexCoords colorSurface,1,0,1.71,-0.3
	EndIf
	UpdateNormals colorEntity
	EntityParent colorEntity,mainEntity
	TextureSucTubeMesh(mainEntity,col,active)
	Return mainEntity
End Function

Function TextureSucTubeMesh(suctube_handle,col=0,active=1)
	active = active Mod 2
	If active=0
		col=(col+16) Mod 32
	Else
		col=col Mod 32
	EndIf
	EntityTexture FindChild(suctube_handle,"SucTubeColor"),GetColorTexture(col)
End Function

Function RedoSucTubeMesh(this.woi)
	; return if invalid model name
	If this\ModelName$<>"!Suctube"
		Return
	EndIf
	colorEntity=FindChild(this\Entity,"SucTubeColor")
	colorSurface=GetSurface(colorEntity,1)
	If this\YawAdjust=(-90*this\ObjData[2] +3600) Mod 360
		; in original position
		dir=0
	Else
		; switched from original
		dir=1
	EndIf
	; point arrow
	If dir=0
		VertexCoords colorSurface,0,-0.3,1.71,-0.3
		VertexCoords colorSurface,1,+0.3,1.71,-0.3
		VertexCoords colorSurface,2,0,1.71,+0.3
	Else
		VertexCoords colorSurface,0,-0.3,1.71,+0.3
		VertexCoords colorSurface,2,+0.3,1.71,+0.3
		VertexCoords colorSurface,1,0,1.71,-0.3
	EndIf
	TextureSucTubeMesh(this\Entity,this\ObjData[0],this\Active)
End Function

Function CreateSucTubeXMesh(tex)
	entity=CreateMesh()
	Surface=CreateSurface(entity)
	nofsegments#=16
	nofarcpoints#=8
	For j=0 To nofarcpoints
		angle2#=(90.0/nofarcpoints)*j
		For i=0 To nofsegments-1
			angle#=-(360.0/nofsegments)/2.0+i*(360.0/nofsegments)
			height#=0.7+1.0*Cos(angle)
			radius#=1.5-1.5*Sin(angle)
			If i Mod 2 =0
				xtex#=0.25
			Else
				xtex#=0.0
			EndIf
			If j Mod 2 =0
				ytex#=19.0
			Else
				ytex#=0.0
			EndIf
			AddVertex (surface,1.5-radius*Cos(angle2),height,-1.5+radius*Sin(angle2),0.25*tex+xtex,(107.0-ytex)/512.0)
		Next
	Next
	For j=0 To nofarcpoints-1
		For i=0 To nofsegments-1
			AddTriangle(surface,j*nofsegments+i,j*nofsegments+((i+1) Mod nofsegments),(j+1)*nofsegments+i)
			AddTriangle(surface,(j+1)*nofsegments+i,j*nofsegments+((i+1) Mod nofsegments),(j+1)*nofsegments+((i+1) Mod nofsegments))
			AddTriangle(surface,j*nofsegments+((i+1) Mod nofsegments),j*nofsegments+i,(j+1)*nofsegments+i)
			AddTriangle(surface,(j+1)*nofsegments+((i+1) Mod nofsegments),j*nofsegments+((i+1) Mod nofsegments),(j+1)*nofsegments+i)
		Next
	Next
	UpdateNormals entity
	EntityTexture entity,GateTexture
	Return entity
End Function

Function VerifyCustomObjectTexture$(textureName$,dir$="UserData/Custom/ObjectTextures/")
	; DebugLog("Veryfing texture :"+textureName$+", "+dir$)
	If FileType(textureName$)=1
		Return textureName$
	EndIf
	If Left(textureName$,1)="?"
		textureName$=Right(textureName$,Len(textureName$)-1)
	EndIf
	If (Lower(Right(textureName$,2)))=".x"
		textureName$=Left(textureName$,Len(textureName$)-2)
	Else If (Lower(Right(textureName$,4)))=".b3d" Or (Lower(Right(textureName$,4)))=".3ds"
		textureName$=Left(textureName$,Len(textureName$)-4)
	EndIf
	textureName$=dir$+textureName$
	Select (Lower(Right(textureName$,4)))
		Case ".dds",".tga",".png",".bmp",".jpg"
			If Not FileType(textureName$)=1
				Locate 0,0
				Color 0,0,0
				Rect 0,0,500,40,True
				Color 255,255,255
				Print "Couldn't load texture: "+textureName$
				Print "Reverting to default..."
				Delay 1200
				textureName$=dir$+"default.jpg"
				If FileType(textureName$)<>1
					textureName$="UserData/Custom/ObjectTextures/default.jpg"
				EndIf
			EndIf
		Default
			If FileType(textureName$+".jpg")=1
				textureName$=textureName$+".jpg"
			Else If FileType(textureName$+".dds")=1
				textureName$=textureName$+".dds"
			Else If FileType(textureName$+".tga")=1
				textureName$=textureName$+".tga"
			Else If FileType(textureName$+".png")=1
				textureName$=textureName$+".png"
			Else If FileType(textureName$+".bmp")=1
				textureName$=textureName$+".bmp"
			Else
				Locate 0,0
				Color 0,0,0
				Rect 0,0,500,40,True
				Color 255,255,255
				Print "Couldn't load any texture from "+textureName$
				Print "Reverting to default..."
				Delay 200
				textureName$=dir$+"default.jpg"
				If FileType(textureName$)<>1
					textureName$="UserData/Custom/ObjectTextures/default.jpg"
				EndIf
			EndIf
	End Select
	; DebugLog("Result :"+textureName$)
	Return textureName$
End Function

Function ApplyCustomObjectTexture(target_handle,TextureName$,dir$="UserData/Custom/ObjectTextures/",cussubtype=0,cusobjtype=0)
	textureName$=VerifyCustomObjectTexture(textureName$,dir$)
	texturePresent=False
	i=0
	For i=0 to NofCustomTextures-1
		If CustomTextureName$(i)=textureName$
			texturePresent=True
			Exit
		EndIf
	Next
	textureExt$=Lower(Right(textureName$,4))
	If texturePresent=False
		i=i+1
		NofCustomTextures=i
		CustomTextureName$(i)=textureName$
		Select textureExt$
			Case ".dds",".tga",".png" ; check if mask exists
				maskName$=Right(textureName$,Len(textureName$)-4)+".mask"+textureExt$
				If FileType(maskName$)=1
					CustomTexture(i)=LoadTexture(textureName$,1+2)
					CustomTextureMask(i)=LoadTexture(maskName$,1+4)
				Else
					CustomTexture(i)=LoadTexture(textureName$,1+4)
					CustomTextureMask(i)=0
				EndIf
			Case ".bmp"
				CustomTexture(i)=LoadTexture(textureName$,1+4)
				CustomTextureMask(i)=0
			Case ".jpg"
				CustomTexture(i)=LoadTexture(textureName$,1)
				CustomTextureMask(i)=0
			Default
				CustomTexture(i)=LoadTexture(textureName$,1)
				CustomTextureMask(i)=0
		End Select
	EndIf
	alphaChild=FindChild(target_handle,"alphaChild")
	If CustomTextureMask(i)<>0
		If alphaChild=0
			alphaChild=CopyEntity(target_handle,target_handle)
			NameEntity alphaChild,"alphaChild"
		EndIf
		EntityTexture target_handle,CustomTextureMask(i)
		EntityTexture alphaChild,CustomTexture(i)
	Else
		If alphaChild<>0
			FreeEntity alphaChild
		EndIf
		EntityTexture target_handle,CustomTexture(i)
		If cussubtype=2 And cusobjtype=160
			For i=1 To CountChildren(target_handle)
				EntityTexture GetChild(target_handle,i),CustomTexture(i)
			Next
		EndIf
	EndIf
End Function

Function VerifyCustomModelMesh$(modelName$,textData0$="Default")
	; DebugLog("Veryfing mesh: "+modelName$+", "+textData0$)
	If modelName$="!CustomModel"
		modelName$=textData0$
	EndIf
	If Left(modelName$,1)="?"
		modelName$="UserData/Custom/Models/"+Right(modelName$,Len(modelName$)-1)
	EndIf
	If Not (Lower(Right(modelName$,2))=".x" Or Lower(Right(modelName$,4))=".b3d" Or Lower(Right(modelName$,4))=".3ds")
		If FileType(modelName$+".x")=1
			modelName$=modelName$+".x"
		Else If FileType(modelName$+".b3d")=1
			modelName$=modelName$+".b3d"
		Else If FileType(modelName$+".3ds")=1
			modelName$=modelName$+".3ds"
		EndIf
	EndIf
	If Not FileType(modelName$)=1
		Locate 0,0
		Color 0,0,0
		Rect 0,0,500,40,True
		Color 255,255,255
		Print "Couldn't load any mesh from "+modelName$
		Print "Reverting to default..."
		Delay 200
		modelName$="UserData/Custom/Models/Default.3ds"
	EndIf
	; DebugLog("Result: "+modelName$)
	Return modelName$
End Function

Function CreateCustomModelMesh(modelName$,textData0$="Default",this.woi)
	modelName$=VerifyCustomModelMesh(modelName$,textData0$)
	i=0
	modelPresent=False
	For i=0 To NofCustomMeshes-1
		If CustomModelName$(i)=modelName$
			modelPresent=True
			Exit
		EndIf
	Next
	; DebugLog("CustomMesh("+Str$(i)+"): "+modelName$)
	If modelPresent=False
		i=i+1
		NofCustomMeshes=i
		CustomModelName$(i)=modelName$
		If this\ObjType=160 And this\SubType=2
			CustomModelMesh(i)=LoadAnimMesh(modelName$,0)
		Else
			CustomModelMesh(i)=LoadMesh(modelName$,0)
		EndIf
		HideEntity CustomModelMesh(i)
	EndIf
	entity=CopyEntity(CustomModelMesh(i))
	Return entity
End Function

Function CreateStinkerModel(this.woi,sizex#,sizey#,sizez#,code$)
	i=GetGameObjectIndex(this)
;	this\TextureName$="models/stinkers/body"+Chr$(96+bodytex)+"1.png"
	this\Entity=CopyEntity(StinkerMesh)
	;EntityFX this\Entity,1
	this\XScale=sizex
	this\YScale=sizey
	this\ZScale=sizez
;	ScaleEntity this\Entity,this\XScale,this\YScale,this\ZScale
	; check if this texture is already loaded - if not, load the set
	tex=(Asc(Mid$(code$,3,1))-48)*100+(Asc(Mid$(code$,4,1))-48)*10+(Asc(Mid$(code$,5,1))-48)
	; shadow/fire stinker (texture applied in LoadObject)
	dtex=0
	If this\ObjType=120 dtex=8
	If tex=5
		tex=1
		this\ObjData[dtex]=5
	EndIf
	If tex=6
		tex=1
		this\ObjData[dtex]=6
	EndIf
	EntityTexture GetChild(this\Entity,3),StinkerTexture(tex-1,0)
	this\Texture=0
	Animate GetChild(this\Entity,3),1,.05,10
	this\CurrentAnim=10
	; now do accessories. Create models based on texture code
	; !T000b000a0 000a0
	;		T000 - which body texture
	;		b which expression
	;		000 - which accessory model, a - which accessory texture for that model, 0 - which bone to attach to
	For j=1 To (Len(code$)-5)/6
		; which bone to attach to
		Select Mid$(code$,j*6+5,1)
			Case "0" ; hat_bone
				bone=FindChild(this\Entity,"hat_bone")
		End Select
		; type and tex
		CreateAccessory(bone,sizex,sizey,sizez,Mid$(code$,j*6+1,4))
		If Mid$(code$,j*6+1,1)="0"
			; only link parent to child if it's a hat (for de-linking in dying animation)
			; the first 100 accessorys are reserved for hats, hence this check
			this\Child=NofObjects-1
		EndIf
		GameObject(NofObjects-1)\Parent=i
	Next
	CreateShadow(i,(sizex+sizey)*11.6)
End Function

; from player/adventures/objects-decorative.bb

Function CreateAccessory(Bone,sizex#,sizey#,sizez#,acccode$)
	i=CreateNewObject()
	GameObject(i)\TileX=0
	GameObject(i)\TileY=0
	GameObject(i)\TileX2=0
	GameObject(i)\TileY2=0
	GameObject(i)\ModelName$="!Hat"
	GameObject(i)\Entity=MyLoadMesh("Data/Models/stinker/accessory"+Left$(acccode$,3)+".3ds",0)
	GameObject(i)\Texture=MyLoadTexture("Data/Models/stinker/accessory"+acccode$+".jpg",4)
	; ScaleEntity GameObject(i)\Entity,sizey,sizez,sizex
	GameObject(i)\XScale=sizey
	GameObject(i)\YScale=sizex
	GameObject(i)\ZScale=sizez
	EntityTexture GameObject(i)\Entity,GameObject(i)\Texture
	GameObject(i)\ObjData[0]=Bone
	GameObject(i)\ObjType=100
End Function

Function CreateShadow(parent,scale#)
	i=CreateNewObject()
	GameObject(i)\Entity=CreateMesh()
	surface=CreateSurface(GameObject(i)\Entity)
	AddVertex (surface,-.49*scale,0.05,.49*scale,0,0)
	AddVertex (surface,.49*scale,0.05,.49*scale,1,0)
	AddVertex (surface,-.49*scale,0.05,-.49*scale,0,1)
	AddVertex (surface,.49*scale,0.05,-.49*scale,1,1)
	AddTriangle surface,0,1,2
	AddTriangle surface,1,3,2
	UpdateNormals GameObject(i)\Entity
	EntityTexture GameObject(i)\Entity,ShadowTexture
	GameObject(i)\ObjType=101
	GameObject(i)\Parent=parent
	EntityBlend GameObject(i)\Entity,2
	EntityFX GameObject(i)\Entity,1
	GameObject(i)\ModelName$="!Shadow"
	;If Not((GameObject(GameObject(i)\Parent)\ZAdjust>=LevelTileExtrusion(GameObject(GameObject(i)\Parent)\TileX,GameObject(GameObject(i)\Parent)\TileY)) And (LevelTileExtrusion(GameObject(GameObject(i)\Parent)\TileX,GameObject(GameObject(i)\Parent)\TileY)>=WaterTileHeight(GameObject(GameObject(i)\Parent)\TileX,GameObject(GameObject(i)\Parent)\TileY) Or GameObject(GameObject(i)\Parent)\ZAdjust<WaterTileHeight(GameObject(GameObject(i)\Parent)\TileX,GameObject(GameObject(i)\Parent)\TileY))) 
	If (LevelTileExtrusion(GameObject(GameObject(i)\Parent)\TileX,GameObject(GameObject(i)\Parent)\TileY)<>0 Or GameMode3D>=1) And GameObject(GameObject(i)\Parent)\ObjType<>40
		GameObject(i)\Z=GameObject(GameObject(i)\Parent)\ZAdjust+0.06
	EndIf
	;EndIf
	;If Not((LevelTileLogic(GameObject(GameObject(i)\Parent)\TileX,GameObject(GameObject(i)\Parent)\TileY)<>4) And (LevelTileLogic(GameObject(GameObject(i)\Parent)\TileX,GameObject(GameObject(i)\Parent)\TileY)<>14))
		;GameObject(i)\Z=GameObject(GameObject(i)\Parent)\ZAdjust+0.06
	;EndIf	
End Function

Function CreateSunSphere2(parent)
	i=CreateNewObject()
	GameObject(i)\ModelName$="!Sun Sphere2"
	GameObject(i)\TextureName$=""
	GameObject(i)\ObjType=442
	GameObject(i)\Entity=CreateSphere()
	ScaleMesh GameObject(i)\Entity,.5,.5,.5	
	;EntityAlpha GameObject(i)\Entity,0.5
	;EntityBlend GameObject(i)\Entity,3
	GameObject(i)\ScaleAdjust=GameObject(Parent)\ScaleAdjust
	GameObject(i)\Texture=0
	;EntityTexture GameObject(i)\Entity,GameObject(i)\Texture
	GameObject(i)\X=GameObject(Parent)\X
	GameObject(i)\Y=GameObject(Parent)\Y
	GameObject(i)\Active=1001
	GameObject(i)\ObjData[7]=GameObject(parent)\ObjData[7]
	GameObject(i)\ObjData[8]=GameObject(Parent)\ObjData[8]
End Function