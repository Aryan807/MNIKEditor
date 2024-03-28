Function BuildObjectModel(Obj.GameObject,x#,y#,z#)

	If Not UseWOI Then FreeObjectModel(Obj\Model)

	If UseWOI
        BuildWOIObjectModel(Obj)
    Else If Obj\Attributes\ModelName$="!Button"
		If Obj\Attributes\LogicSubType=16 And Obj\Attributes\Data2=1 Then Obj\Attributes\LogicSubType=17
		If Obj\Attributes\LogicSubType=17 And Obj\Attributes\Data2=0 Then Obj\Attributes\LogicSubType=16
		If Obj\Attributes\LogicSubType=16+32 And Obj\Attributes\Data2=1 Then Obj\Attributes\LogicSubType=17+32
		If Obj\Attributes\LogicSubType=17+32 And Obj\Attributes\Data2=0 Then Obj\Attributes\LogicSubType=16+32

		Obj\Model\Entity=CreateButtonMesh2(Obj\Attributes\LogicSubType,Obj\Attributes\Data0,Obj\Attributes\Data1,Obj\Attributes\Data2,Obj\Attributes\Data3)

	Else If Obj\Attributes\ModelName$="!CustomModel"

		CustomModelNameToUse$=Obj\Attributes\TextData0
		If FileType("UserData\Custom\Models\"+Obj\Attributes\TextData0+".3ds")<>1 Or FileType("UserData\Custom\Models\"+Obj\Attributes\TextData0+".jpg")<>1
			ShowMessageOnce("Couldn't Load 3ds/jpg. Reverting to 'Default' Custom Model.",2000)

			CustomModelNameToUse$="Default"
		EndIf
		Obj\Model\Entity=LoadMesh("UserData\Custom\Models\"+CustomModelNameToUse$+".3ds")
		Obj\Model\Texture=LoadTexture("UserData\Custom\Models\"+CustomModelNameToUse$+".jpg")
		EntityTexture Obj\Model\Entity,Obj\Model\Texture

	Else If Obj\Attributes\ModelName$="!Teleport"
		Obj\Model\Entity=CreateTeleporterMesh2(Obj\Attributes\Data0)
	Else If Obj\Attributes\ModelName$="!Item"
		Obj\Model\Entity=CreatePickupItemMesh2(Obj\Attributes\Data2)
	; Q - player NPC functionality
	Else If Obj\Attributes\ModelName$="!Stinker" Or IsModelNPC(Obj\Attributes\ModelName$)
		Obj\Model\Entity=CopyEntity(StinkerMesh)

		If Obj\Attributes\Data0=5
			Obj\Model\Texture=Waterfalltexture(0) ;MyLoadTexture("Data\leveltextures\waterfall.jpg",1)
		Else If Obj\Attributes\Data0=6
			Obj\Model\Texture=Waterfalltexture(1) ;MyLoadTexture("Data\leveltextures\waterfalllava.jpg",1)
		Else If Obj\Attributes\Data0<1 Or Obj\Attributes\Data0>8 Or Obj\Attributes\Data1<0 Or Obj\Attributes\Data1>4
			; prevent out-of-bounds texture usage
			UseErrorColor(GetChild(Obj\Model\Entity,3))
		Else
			Obj\Model\Texture=MyLoadTexture("data/models/stinker/body00"+Str$(Obj\Attributes\Data0)+Chr$(65+Obj\Attributes\Data1)+".jpg",1)
		EndIf

		If Obj\Model\Texture>0
			EntityTexture GetChild(Obj\Model\Entity,3),Obj\Model\Texture
		EndIf

		If Obj\Attributes\Data2>0	; hat
			Obj\Model\HatEntity=CreateAccEntity(Obj\Attributes\Data2)
			Obj\Model\HatTexture=CreateHatTexture(Obj\Attributes\Data2,Obj\Attributes\Data3)

			;TransformAccessoryEntityOntoBone(Obj\Model\HatEntity,Obj\Model\Entity)
		EndIf

		If Obj\Attributes\Data4>0 ;100 ; acc
			Obj\Model\AccEntity=CreateAccEntity(Obj\Attributes\Data4)
			Obj\Model\AccTexture=CreateGlassesTexture(Obj\Attributes\Data4,Obj\Attributes\Data5)

			;TransformAccessoryEntityOntoBone(Obj\Model\AccEntity,Obj\Model\Entity)
		EndIf

	Else If Obj\Attributes\ModelName$="!ColourGate"
		Obj\Model\Entity=CreateColourGateMesh2(Obj\Attributes\Data2,Obj\Attributes\Data0)
	Else If Obj\Attributes\ModelName$="!Transporter"
		Obj\Model\Entity=CreateTransporterMesh2(Obj\Attributes\Data0,3)
		RotateMesh Obj\Model\Entity,0,90*Obj\Attributes\Data2,0

	Else If Obj\Attributes\ModelName$="!Conveyor"
		If Obj\Attributes\Data4=4
			Obj\Model\Entity=CreateCloudMesh2(Obj\Attributes\Data0)
		Else
			Obj\Model\Entity=CreateTransporterMesh2(Obj\Attributes\Data0,Obj\Attributes\Data4)
		EndIf
		RotateMesh Obj\Model\Entity,0,-90*Obj\Attributes\Data2,0
		If Obj\Attributes\LogicType=46 ScaleMesh Obj\Model\Entity,.5,.5,.5

	Else If Obj\Attributes\ModelName$="!Autodoor"
		Obj\Model\Entity=CopyEntity(AutodoorMesh)

	Else If Obj\Attributes\ModelName$="!Key"
		Obj\Model\Entity=CreateKeyMesh2(Obj\Attributes\Data0)
	Else If Obj\Attributes\ModelName$="!KeyCard"
		Obj\Model\Entity=CreateKeyCardMesh2(Obj\Attributes\Data0)

	Else If Obj\Attributes\ModelName$="!StinkerWee"
		Obj\Model\Entity=CopyEntity(StinkerWeeMesh)

		If Obj\Attributes\LogicType=120 ; Wee Stinker
			If Obj\Attributes\Data8>=0 And Obj\Attributes\Data8<=2
				EntityTexture Obj\Model\Entity,StinkerWeeTexture(Obj\Attributes\Data8+1)
			Else
				Obj\Model\Entity=RemoveMD2Texture(Obj\Model\Entity,"data/models/stinkerwee/stinkerwee.md2")
				UseErrorColor(Obj\Model\Entity)
			EndIf
		EndIf
	Else If Obj\Attributes\ModelName$="!Cage"
		Obj\Model\Entity=CopyEntity(CageMesh)
	Else If Obj\Attributes\ModelName$="!StarGate"
		Obj\Model\Entity=CopyEntity(StarGateMesh)
	Else If Obj\Attributes\ModelName$="!Scritter"
		Obj\Model\Entity=CopyEntity(ScritterMesh)
		If Obj\Attributes\Data0>=0 And Obj\Attributes\Data0<=6
			EntityTexture Obj\Model\Entity,ScritterTexture(Obj\Attributes\Data0)
		Else
			UseErrorColor(Obj\Model\Entity)
		EndIf
	Else If Obj\Attributes\ModelName$="!RainbowBubble"
		Obj\Model\Entity=CreateSphere()
		;ScaleMesh Obj\Model\Entity,.4,.4,.4
		;PositionMesh Obj\Model\Entity,0,1,0
		ScaleMesh Obj\Model\Entity,.5,.5,.5
		EntityTexture Obj\Model\Entity,Rainbowtexture2

	Else If Obj\Attributes\ModelName$="!IceBlock"
		Obj\Model\Entity=CreateIceBlockMesh2(Obj\Attributes\Data3)

	Else If Obj\Attributes\ModelName$="!PlantFloat"
		Obj\Model\Entity=CreatePlantFloatMesh()
	Else If Obj\Attributes\ModelName$="!IceFloat"
		Obj\Model\Entity=CreateIceFloatMesh()

	Else If Obj\Attributes\ModelName$="!Chomper"
		Obj\Model\Entity=CopyEntity(ChomperMesh)
		If Obj\Attributes\LogicSubType=1
			EntityTexture Obj\Model\Entity,WaterChomperTexture
		Else If Obj\Attributes\Data1=3
			EntityTexture Obj\Model\Entity,MechaChomperTexture
		Else
			EntityTexture Obj\Model\Entity,ChomperTexture
		EndIf
	Else If Obj\Attributes\ModelName$="!Bowler"
		Obj\Model\Entity=CopyEntity(BowlerMesh)
		Direction=Obj\Attributes\Data0
		If Obj\Attributes\Data1<>2
			Direction=Direction*2
		EndIf
		Obj\Attributes\YawAdjust=(-45*Direction +3600) Mod 360
	Else If Obj\Attributes\ModelName$="!Turtle"
		Obj\Model\Entity=CopyEntity(TurtleMesh)
		Obj\Attributes\YawAdjust=(-90*Obj\Attributes\Data0 +3600) Mod 360
	Else If Obj\Attributes\ModelName$="!Thwart"
		Obj\Model\Entity=CopyEntity(ThwartMesh)
		If Obj\Attributes\Data2>=0 And Obj\Attributes\Data2<=7
			EntityTexture Obj\Model\Entity,ThwartTexture(Obj\Attributes\Data2)
		Else
			Obj\Model\Entity=RemoveMD2Texture(Obj\Model\Entity,"data\models\thwart\thwart05.md2")
			UseErrorColor(Obj\Model\Entity)
		EndIf
	Else If Obj\Attributes\ModelName$="!Tentacle"
		Obj\Model\Entity=CopyEntity(TentacleMesh)
		Animate GetChild(Obj\Model\Entity,3),1,.1,1,0
	Else If Obj\Attributes\ModelName$="!Lurker"
		Obj\Model\Entity=CopyEntity(LurkerMesh)
	Else If Obj\Attributes\ModelName$="!Ghost"
		Obj\Model\Entity=CopyEntity(GhostMesh)
	Else If Obj\Attributes\ModelName$="!Wraith"
		Obj\Model\Entity=CopyEntity(WraithMesh)
		If Obj\Attributes\Data2>=0 And Obj\Attributes\Data2<=2
			EntityTexture Obj\Model\Entity,WraithTexture(Obj\Attributes\Data2)
		Else
			Obj\Model\Entity=RemoveEntityTexture(Obj\Model\Entity)
			UseErrorColor(Obj\Model\Entity)
		EndIf

	Else If Obj\Attributes\ModelName$="!Crab"
		Obj\Model\Entity=CopyEntity(CrabMesh)
		If Obj\Attributes\LogicSubType=0 Then EntityTexture Obj\Model\Entity,CrabTexture2
	Else If Obj\Attributes\ModelName$="!Troll"
		Obj\Model\Entity=CopyEntity(TrollMesh)
	Else If Obj\Attributes\ModelName$="!Kaboom"
		Obj\Model\Entity=CopyEntity(KaboomMesh)
		If Obj\Attributes\Data0>=1 And Obj\Attributes\Data0<=5
			EntityTexture Obj\Model\Entity,KaboomTexture(Obj\Attributes\Data0)
		Else
			Obj\Model\Entity=RemoveMD2Texture(Obj\Model\Entity,"data\models\kaboom\kaboom.md2")
			UseErrorColor(Obj\Model\Entity)
		EndIf
	Else If Obj\Attributes\ModelName$="!BabyBoomer"
		Obj\Model\Entity=CopyEntity(KaboomMesh)
		EntityTexture Obj\Model\Entity,KaboomTexture(1)

	Else If Obj\Attributes\ModelName$="!FireFlower"
		Obj\Model\Entity=CopyEntity(FireFlowerMesh)
		If Obj\Attributes\LogicSubType<>1
			Obj\Attributes\YawAdjust=(-45*Obj\Attributes\Data0 +3600) Mod 360
		Else
			Obj\Attributes\YawAdjust=0
		EndIf
		If Obj\Attributes\Data1=1
			EntityTexture Obj\Model\Entity,FireFlowerTexture2
		EndIf

	Else If Obj\Attributes\ModelName$="!BurstFlower"
		Obj\Model\Entity=CopyEntity(BurstFlowerMesh)

	Else If Obj\Attributes\ModelName$="!Busterfly"
		Obj\Model\Entity=CopyEntity(BusterflyMesh)
		;AnimateMD2 Obj\Model\Entity,2,.4,2,9

	Else If Obj\Attributes\ModelName$="!GlowWorm"  Or Obj\Attributes\ModelName$="!Zipper"
		Obj\Model\Entity=CreateSphere(12)
		ScaleMesh Obj\Model\Entity,.1,.1,.1
		EntityColor Obj\Model\Entity,Obj\Attributes\Data5,Obj\Attributes\Data6,Obj\Attributes\Data7
	Else If Obj\Attributes\ModelName$="!Void"
		;Obj\Model\Entity=CreateSphere(12)
		;ScaleMesh Obj\Model\Entity,.4,.15,.4
		Obj\Model\Entity=CreateVoidMesh()
	Else If Obj\Attributes\ModelName$="!Rubberducky"
		Obj\Model\Entity=CopyEntity(RubberduckyMesh)

	Else If Obj\Attributes\ModelName$="!Barrel1"
		Obj\Model\Entity=CopyEntity(BarrelMesh)
		EntityTexture Obj\Model\Entity,BarrelTexture1
	Else If Obj\Attributes\ModelName$="!Barrel2"
		Obj\Model\Entity=CopyEntity(BarrelMesh)
		EntityTexture Obj\Model\Entity,BarrelTexture2
	Else If Obj\Attributes\ModelName$="!Barrel3"
		Obj\Model\Entity=CopyEntity(BarrelMesh)
		EntityTexture Obj\Model\Entity,BarrelTexture3
	Else If Obj\Attributes\ModelName$="!Cuboid"
		Obj\Model\Entity=CreateCube()
		ScaleMesh Obj\Model\Entity,0.4,0.4,0.4
		PositionMesh Obj\Model\Entity,0,0.5,0
		If Obj\Attributes\Data0<0 Or Obj\Attributes\Data0>8 Then Obj\Attributes\Data0=0
		EntityTexture Obj\Model\Entity,OldTeleporterTexture(Obj\Attributes\Data0)

	Else If Obj\Attributes\ModelName$="!Prism"
		Obj\Model\Entity=CopyEntity(PrismMesh)
		EntityTexture Obj\Model\Entity,PrismTexture

	Else If  Obj\Attributes\ModelName$="!Obstacle10"
		Obj\Model\Entity=CopyEntity(  ObstacleMesh(10 ))
		EntityTexture Obj\Model\Entity, MushroomTex(  (Abs(Obj\Attributes\Data0)) Mod 3)

	Else If  Obj\Attributes\ModelName$="!Obstacle51" Or Obj\Attributes\ModelName$="!Obstacle55" Or Obj\Attributes\ModelName$="!Obstacle59"
		ObstacleId=ObstacleNameToObstacleId(Obj\Attributes\ModelName$)
		Obj\Model\Entity=TryGetObstacleMesh(ObstacleId+Obj\Attributes\Data0)
		Obj\Model\Entity=TryUseObstacleTexture(Obj\Model\Entity,ObstacleId+Obj\Attributes\Data1)

	Else If Left$(Obj\Attributes\ModelName$,9)="!Obstacle"
		ObstacleId=ObstacleNameToObstacleId(Obj\Attributes\ModelName$)
		Obj\Model\Entity=TryGetObstacleMesh(ObstacleId)

	Else If Obj\Attributes\ModelName$="!WaterFall"
		Obj\Model\Entity=CreateWaterFallMesh2(Obj\Attributes\Data0)
	Else If Obj\Attributes\ModelName$="!Star"
		Obj\Model\Entity=CopyEntity(StarMesh)
		EntityTexture Obj\Model\Entity,GoldStarTexture
	Else If Obj\Attributes\ModelName$="!Wisp"
		Obj\Model\Entity=CopyEntity(StarMesh)
		If Obj\Attributes\Data0>=0 And Obj\Attributes\Data0<=9
			EntityTexture Obj\Model\Entity,WispTexture(Obj\Attributes\Data0)
		Else
			Obj\Model\Entity=RemoveEntityTexture(Obj\Model\Entity)
			UseErrorColor(Obj\Model\Entity)
		EndIf

	Else If Obj\Attributes\ModelName$="!Portal Warp"
		Obj\Model\Entity=CopyEntity(PortalWarpMesh)
		If Obj\Attributes\Data1=0
			EntityTexture Obj\Model\Entity,StarTexture
		Else
			EntityTexture Obj\Model\Entity,RainbowTexture
		EndIf

	Else If Obj\Attributes\ModelName$="!Sun Sphere1"
		Obj\Model\Entity=CreateSphere()
		EntityColor Obj\Model\Entity,Obj\Attributes\Data0,Obj\Attributes\Data1,Obj\Attributes\Data2
		EntityBlend Obj\Model\Entity,3

	Else If Obj\Attributes\ModelName$="!Sun Sphere2"
		Obj\Model\Entity=CreateSphere()
		ScaleMesh Obj\Model\Entity,.5,.5,.5

	Else If Obj\Attributes\ModelName$="!Coin"
		Obj\Model\Entity=CopyEntity(CoinMesh)
		EntityTexture Obj\Model\Entity,GoldCoinTexture
		If Obj\Attributes\LogicType=425 EntityTexture Obj\Model\Entity,Retrorainbowcointexture
	Else If Obj\Attributes\ModelName$="!Token"
		Obj\Model\Entity=CopyEntity(CoinMesh)
		EntityTexture Obj\Model\Entity,TokenCoinTexture
	Else If Obj\Attributes\ModelName$="!Gem"
		;If Obj\Attributes\Data0<0 Or Obj\Attributes\Data0>2 Then Obj\Attributes\Data0=0
		;If Obj\Attributes\Data1<0 Or Obj\Attributes\Data1>7 Then Obj\Attributes\Data1=0

		; Note that the vanilla WA3E player will kill you without hesitation if you have a Data0 (gem mesh) outside this range.
		Data0=Obj\Attributes\Data0
		If Data0<0 Or Data0>2 Then Data0=0

		Obj\Model\Entity=CopyEntity(GemMesh(Data0))

		Data1=Obj\Attributes\Data1
		If Data1<0 Or Data1>8
			UseErrorColor(Obj\Model\Entity)
		Else
			EntityTexture Obj\Model\Entity,OldTeleporterTexture(Data1)
		EndIf
	Else If Obj\Attributes\ModelName$="!Crystal"
		Obj\Model\Entity=CopyEntity(GemMesh(2))
		If Obj\Attributes\Data0=0
			EntityTexture Obj\Model\Entity,rainbowtexture
		Else
			EntityTexture Obj\Model\Entity,ghosttexture
		EndIf

	Else If Obj\Attributes\ModelName$="!Sign"
		Obj\Model\Entity=CreateSignMesh(Obj\Attributes\Data0,Obj\Attributes\Data1)

	Else If Obj\Attributes\ModelName$="!CustomItem"
		Obj\Model\Entity=CreateCustomItemMesh2(Obj\Attributes\Data0)

	Else If Obj\Attributes\ModelName$="!SteppingStone"
		Obj\Model\Entity=MyLoadMesh("data\models\bridges\cylinder1.b3d",0)
		If Obj\Attributes\Data0<0 Or Obj\Attributes\Data0>3
			UseErrorColor(Obj\Model\Entity)
		Else
			EntityTexture Obj\Model\Entity,SteppingStoneTexture(Obj\Attributes\Data0)
		EndIf
	Else If Obj\Attributes\ModelName$="!Spring"
		Obj\Model\Entity=MyLoadMesh("data\models\bridges\cylinder1.b3d",0)
		RotateMesh Obj\Model\Entity,90,0,0
		Obj\Attributes\YawAdjust=(-45*Obj\Attributes\Data2 +3600) Mod 360

		EntityTexture Obj\Model\Entity,Springtexture
	Else If Obj\Attributes\ModelName$="!Suctube"
		Obj\Model\Entity=CreateSuctubeMesh2(Obj\Attributes\Data3,Obj\Attributes\Data0,True)

		Obj\Attributes\YawAdjust=(-90*Obj\Attributes\Data2 +3600) Mod 360

		RedosuctubeMesh2(Obj\Model\Entity, Obj\Attributes\Data0, Obj\Attributes\Active, Obj\Attributes\Data2, Obj\Attributes\YawAdjust)

	Else If Obj\Attributes\ModelName$="!SuctubeX"
		Obj\Model\Entity=CreateSuctubeXMesh2(Obj\Attributes\Data3)
		Obj\Attributes\YawAdjust=(-90*Obj\Attributes\Data2 +3600) Mod 360

	Else If Obj\Attributes\ModelName$="!FlipBridge"
		Obj\Model\Entity=CreateFlipBridgeMesh2(Obj\Attributes\Data0)
		Obj\Attributes\YawAdjust=(-45*Obj\Attributes\Data2 +3600) Mod 360

	Else If Obj\Attributes\ModelName$="!Door"
		Obj\Model\Entity=MyLoadmesh("data\models\houses\door01.3ds",0)

	Else If Obj\Attributes\ModelName$="!Cylinder"
		Obj\Model\Entity=CopyEntity(cylinder)

	Else If Obj\Attributes\ModelName$="!Square"
		Obj\Model\Entity=MyLoadmesh("Data\models\squares\square1.b3d",0)

	Else If Obj\Attributes\ModelName$="!SpellBall"
		Obj\Model\Entity=CreateSpellBallMesh2(7) ; use white magic spellball mesh

	Else If Obj\Attributes\ModelName$="!Fence1"
		Obj\Model\Entity=CopyEntity(fence1)
	Else If Obj\Attributes\ModelName$="!Fence2"
		Obj\Model\Entity=CopyEntity(fence2)
	Else If Obj\Attributes\ModelName$="!Fencepost"
		Obj\Model\Entity=CopyEntity(fencepost)
	Else If Obj\Attributes\ModelName$="!Fountain"
		Obj\Model\Entity=MyLoadmesh("data\models\houses\fountain01.b3d",0)
		EntityTexture Obj\Model\Entity,FountainTexture

	Else If Obj\Attributes\ModelName$="!Retrobox"
		Obj\Model\Entity=CopyEntity(RetroBoxMesh)

	Else If Obj\Attributes\ModelName$="!Retrocoily"
		Obj\Model\Entity=CopyEntity(RetroCoilyMesh)

	Else If Obj\Attributes\ModelName$="!Retroscouge"
		Obj\Model\Entity=CopyEntity(RetroScougeMesh)
		Obj\Attributes\YawAdjust=(-90*Obj\Attributes\Data0 +3600) Mod 360

	Else If Obj\Attributes\ModelName$="!Retrozbot"
		Obj\Model\Entity=CopyEntity(RetroZbotMesh)
		Obj\Attributes\YawAdjust=(-90*Obj\Attributes\Data0 +3600) Mod 360

	Else If Obj\Attributes\ModelName$="!Retroufo"
		Obj\Model\Entity=CopyEntity(RetroUFOMesh)
		Obj\Attributes\YawAdjust=(-90*Obj\Attributes\Data0 +3600) Mod 360

	Else If Obj\Attributes\ModelName$="!Retrolasergate"
		Obj\Model\Entity=CreateretrolasergateMesh2(Obj\Attributes\Data0)

	Else If Obj\Attributes\ModelName$="!Weebot"
		Obj\Model\Entity=CopyEntity(WeebotMesh)
		Obj\Attributes\YawAdjust=(-90*Obj\Attributes\Data0 +3600) Mod 360

	Else If Obj\Attributes\ModelName$="!Zapbot"
		Obj\Model\Entity=CopyEntity(ZapbotMesh)
		Obj\Attributes\YawAdjust=(-90*Obj\Attributes\Data0 +3600) Mod 360

	Else If Obj\Attributes\ModelName$="!Pushbot"
		Obj\Model\Entity=CreatePushbotMesh(Obj\Attributes\Data0,Obj\Attributes\Data3)

		If Obj\Attributes\LogicType=432 ; Moobot
			Obj\Attributes\YawAdjust=-Obj\Attributes\Data2*90
		Else
			Obj\Attributes\YawAdjust=0 ; Unfortunately hardcoded in adventures.bb.
		EndIf
	Else If Obj\Attributes\ModelName="!FloingOrb" ; not toObj\Attributes\YawAdjustingBubble
		Obj\Model\Entity=CreateSphere()
		ScaleMesh Obj\Model\Entity,.3,.3,.3
		EntityColor Obj\Model\Entity,255,0,0

	Else If Obj\Attributes\ModelName="!MagicMirror"
		Obj\Model\Entity=CreateMagicMirrorMesh()

	Else If Obj\Attributes\ModelName$="!SkyMachineMap"
		Obj\Model\Entity=CreateCube()
		ScaleMesh Obj\Model\Entity,2.5,.01,2.5
		PositionMesh Obj\Model\Entity,0,0,-1
		EntityTexture Obj\Model\Entity,SkyMachineMapTexture
		EntityBlend Obj\Model\Entity,3

	Else If Obj\Attributes\ModelName$="!GrowFlower"
		Obj\Model\Entity=CreateGrowFlowerMesh(Obj\Attributes\Data0)

	Else If Obj\Attributes\ModelName$="!FloingBubble"
		Obj\Model\Entity=CreateFloingBubbleMesh()

	Else If Obj\Attributes\ModelName$="!None"
		Obj\Model\Entity=CreateNoneMesh()

		If Obj\Attributes\LogicType=50 ; spellball
			UseMagicColor(Obj\Model\Entity,Obj\Attributes\LogicSubType)
		EndIf

	Else ;unknown model
		Obj\Model\Entity=CreateErrorMesh()

	EndIf

	If Obj\Attributes\ModelName$="!FlipBridge"
		TextureTarget=GetChild(Obj\Model\Entity,1)
	Else
		TextureTarget=Obj\Model\Entity
	EndIf

	If Obj\Attributes\TexName$="!None"
		Obj\Model\Texture=0
	Else If Obj\Attributes\TexName$="!Door"
		If Obj\Attributes\Data5<0 Then Obj\Attributes\Data5=0
		If Obj\Attributes\Data5>2 Then Obj\Attributes\Data5=2
		If DoorTexture(Obj\Attributes\Data5)=0 Then Obj\Attributes\Data5=0
		EntityTexture TextureTarget,DoorTexture(Obj\Attributes\Data5)
	Else If Obj\Attributes\TexName$="!Cottage"
		If Obj\Attributes\Data5<0 Then Obj\Attributes\Data5=0
		If CottageTexture(Obj\Attributes\Data5)=0 Then Obj\Attributes\Data5=0
		EntityTexture TextureTarget,CottageTexture(Obj\Attributes\Data5)
	Else If Obj\Attributes\TexName$="!Townhouse"
		If Obj\Attributes\Data5<0 Then Obj\Attributes\Data5=0
		If HouseTexture(Obj\Attributes\Data5)=0 Then Obj\Attributes\Data5=0
		EntityTexture TextureTarget,HouseTexture(Obj\Attributes\Data5)
	Else If Obj\Attributes\TexName$="!Windmill"
		If Obj\Attributes\Data5<0 Then Obj\Attributes\Data5=0
		If OldWindmillTexture(Obj\Attributes\Data5)=0 Then Obj\Attributes\Data5=0
		EntityTexture TextureTarget,OldWindmillTexture(Obj\Attributes\Data5)
	Else If Obj\Attributes\TexName$="!Fence"
		If Obj\Attributes\Data5<0 Then Obj\Attributes\Data5=0
		If OldFenceTexture(Obj\Attributes\Data5)=0 Then Obj\Attributes\Data5=0
		EntityTexture TextureTarget,OldFenceTexture(Obj\Attributes\Data5)
	Else If Obj\Attributes\TexName$="!FireTrap"
		EntityTexture TextureTarget,FireTrapTexture

	Else If Left$(Obj\Attributes\TexName$,2)="!T"

		EntityTexture TextureTarget,EditorStinkerTexture

		For i=1 To CountChildren(TextureTarget)
			child=GetChild(TextureTarget,i)
			EntityTexture child,EditorStinkerTexture
		Next
	Else If Obj\Attributes\TexName$="!GloveTex"
		EntityTexture TextureTarget,GloveTex

	Else If Left(Obj\Attributes\TexName$,1)="?"
		; custom texture For existing objects
		If Lower(Right(Obj\Attributes\TexName$,4))=".jpg" Or Lower(Right(Obj\Attributes\TexName$,4))=".bmp" Or Lower(Right(Obj\Attributes\TexName$,4))=".png"
			tname$="UserData\Custom\Objecttextures\"+Right(Obj\Attributes\TexName$,Len(Obj\Attributes\TexName$)-1)
		Else
			tname$="UserData\Custom\Objecttextures\"+Right(Obj\Attributes\TexName$,Len(Obj\Attributes\TexName$)-1)+".jpg"
		EndIf
		If FileType(tname$)<>1
			tname$="UserData\Custom\Objecttextures\default.jpg"
			Obj\Attributes\TexName$="?Default"
		EndIf

		If Lower(Right(tname$,4))=".png"
			; if png load texture with alpha map
			Obj\Model\Texture=LoadTexture(tname$,3)
		Else
			Obj\Model\Texture=LoadTexture(tname$,4)
		EndIf
		EntityTexture TextureTarget,Obj\Model\Texture

	Else If Obj\Attributes\TexName$<>"" And Obj\Attributes\TexName$<>"!None" And Left$(Obj\Attributes\TexName$,1)<>"!"  And Obj\Attributes\ModelName$<>"!Button"
		If myFileType(Obj\Attributes\TexName$)=1 Or FileType(Obj\Attributes\TexName$)=1
			Obj\Model\Texture=myLoadTexture(Obj\Attributes\TexName$,4)
			EntityTexture TextureTarget,Obj\Model\Texture
		Else
			Locate 0,0
			Color 255,255,255
			Print "WARNING!"
			Print "Couldn't load texture: " + Obj\Attributes\TexName$
			Print "The adventure may be unplayable in game"
			Delay 2000
		EndIf

	EndIf

	;If ObjectAdjusterScaleAdjust\Absolute
	;	If Obj\Attributes\ScaleAdjust=0.0 Then Obj\Attributes\ScaleAdjust=1.0
	;EndIf

	If Obj\Attributes\ModelName$<>"!None"
		ScaleEntity Obj\Model\Entity,Obj\Attributes\XScale*Obj\Attributes\ScaleAdjust,Obj\Attributes\ZScale*Obj\Attributes\ScaleAdjust,Obj\Attributes\YScale*Obj\Attributes\ScaleAdjust
		;RotateEntity Obj\Model\Entity,Obj\Attributes\PitchAdjust,Obj\Attributes\YawAdjust,Obj\Attributes\RollAdjust
		RotateEntity Obj\Model\Entity,0,0,0
		TurnEntity Obj\Model\Entity,Obj\Attributes\PitchAdjust,0,Obj\Attributes\RollAdjust
		TurnEntity Obj\Model\Entity,0,Obj\Attributes\YawAdjust,0

		If Obj\Attributes\ModelName$="!Kaboom" Or Obj\Attributes\ModelName$="!BabyBoomer" Then TurnEntity Obj\Model\Entity,0,90,0

	;	PositionEntity Obj\Model\Entity,Obj\Attributes\XAdjust,Obj\Attributes\ZAdjust+Obj\Position\Z,-Obj\Attributes\YAdjust

	EndIf

	If Obj\Attributes\LogicType=200 Or Obj\Attributes\LogicType=201 ; glovecharge or glovedischarge
		If EntityClass(Obj\Model\Entity)="Mesh"
			If CountSurfaces(Obj\Model\Entity)<>0
				EntityFX Obj\Model\Entity,2

				TheSurface=GetSurface(Obj\Model\Entity,1)
				For ii=0 To 3
					Col=Obj\Attributes\Data0
					red=GetMagicColor(Col,0)
					green=GetMagicColor(Col,1)
					blue=GetMagicColor(Col,2)
					VertexColor TheSurface,ii,red,green,blue
				Next
			EndIf
		EndIf
	EndIf

	PositionEntityWithXYZAdjust(Obj\Model\Entity,x#,y#,z#,Obj\Attributes)

	BuildObjectAccessories(Obj)

End Function