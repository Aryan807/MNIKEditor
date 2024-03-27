Function PopulateGlobalGraphics(mode) ; 0 = editor, 1 = player
	; Load Textures
	; =================
	CloudTexture=MyLoadTexture("Data/NewGraphics/cloud.png",1+4); MyLoadTexture("Data/Graphics/cloud.jpg",1+4)

	KeyCardTexture(0)=MyLoadTexture("Data/NewGraphics/keycardbottom.png",1+4)
	KeyCardTexture(1)=MyLoadTexture("Data/NewGraphics/keycardtop.png",1+4)

	; Teleport
	NewTeleporterTexture=MyLoadTexture("Data/NewModels/Teleport/teleport.png",1)
	For i=0 To 8
		TeleporterTexture(i)=myLoadTexture("data/models/teleport/teleport"+Str$(i)+".jpg",1)
	Next
	; TextureBlend TeleporterTexture,3
	; TextureCoords TeleporterTexture,1 ; !!! Yields GPU-dependent results !!!

	; FireTrap
	FireTrapTexture=MyLoadTexture("Data/Models/Squares/firetrap.bmp",1+4)

	; WaterFall
	WaterFallTexture(0)=MyLoadTexture("Data/LevelTextures/waterfall.jpg",1)
	WaterFallTexture(1)=MyLoadTexture("Data/LevelTextures/waterfalllava.jpg",1)
	WaterFallTexture(2)=MyLoadTexture("Data/LevelTextures/waterfallgreen.jpg",1)

	; Spring
	If mode=1 And WAEpisode<>0
		SpringTexture=MyLoadTexture("Data/models/bridges/spring.jpg",1)
	Else
		SpringTexture=MyLoadTexture("Data/NewModels/Bridges/spring.png",1+4)
	EndIf

	; SteppingStones
	For i=0 To 3
		SteppingStoneTexture(i)=MyLoadTexture("Data/Models/bridges/bridge"+i+".jpg",1)
	Next
	StepStoneTexture=MyLoadTexture("Data/NewModels/bridges/stepstone.png",1+4)

	; Void
	VoidTexture=MyLoadTexture("Data/Models/void/void.jpg",1)

	; GloveTex
	GloveTex=MyLoadTexture("Data/Models/Squares/glove.bmp",1+4)

	; GrowFlowers, Floingbubbles
	PlasmaTexture=MyLoadTexture("Data/Models/other/growflower.jpg",1)
	FloingTexture=MyLoadTexture("Data/Models/other/floingbubble.jpg",1)
	Flashbubble=MyLoadTexture("Data/NewGraphics/flashbubble.jpg",1)

	; Pushbot
	PushbotTexture=MyLoadTexture("Data/Graphics/pushbot.bmp",1)
	KillerMoobot=MyLoadTexture("Data/NewModels/KillerMoobot/moobot1.png",1)

	; Mirror
	For i=0 To 4
		MirrorTexture(i)=MyLoadTexture("Data/Models/mirror/mirror"+Str$(i+1)+".jpg",1)
	Next
	SkyMachineMapTexture=MyLoadTexture("Data/Models/other/projected.jpg",1)

	; Shadows
	ShadowTexture=MyLoadTexture("Data/Graphics/shadow0.bmp",1)

	If mode<>0 And WAEpisode<>1
		PositionEntity cube2,.3,-1,4 ; loading bar: 30%
		RenderWorld
		Flip
	EndIf

	; PreLoad Models
	; ==================
	; Stinker
	StinkerMesh=MyLoadAnimMesh("Data/Models/stinker/body.b3d")
	;For tex=0 To 7
	;	If tex<>4 And tex<>5
	;		For expr=0 To 4
	;			StinkerTexture(tex,expr)=MyLoadTexture("Data/Models/stinker/body00"+Str$(tex+1)+Chr$(65+expr)+".jpg",1)
	;		Next
	;		StinkerTexture(tex,5)=MyLoadTexture("Data/NewModels/stinker/body00"+Str$(tex+1)+Chr$(70)+".jpg",1)
	;	EndIf
	;Next
	;For expr=0 To 5
	;	StinkerTexture(8,expr)=MyLoadTexture("Data/NewModels/stinker/body00"+Str$(8+1)+Chr$(65+expr)+".jpg",1)
	;Next
	StinkerTexture=MyLoadTexture("data\models\stinker\body001a.jpg",1)
	StinkerSmokedTexture=MyLoadTexture("Data/Models/stinker/bodysmoked.jpg",1)
	If mode=0
		;EntityTexture GetChild(StinkerMesh,3),StinkerTexture
	Else
		UpdateNormals GetChild(StinkerMesh,3)
		ExtractAnimSeq GetChild(StinkerMesh,3),1,20		; 1 - waddle, speed .2
		ExtractAnimSeq GetChild(StinkerMesh,3),21,40	; 2 - walk, speed .2
		ExtractAnimSeq GetChild(StinkerMesh,3),41,60	; 3 - run, speed .2
		ExtractAnimSeq GetChild(StinkerMesh,3),61,100	; 4 - spell, speed .2
		ExtractAnimSeq GetChild(StinkerMesh,3),61,80	; 5 - spell on, speed .2
		ExtractAnimSeq GetChild(StinkerMesh,3),80,84	; 6 - spell hold, speed .2
		ExtractAnimSeq GetChild(StinkerMesh,3),84,100	; 7 - spell off, speed .2
		ExtractAnimSeq GetChild(StinkerMesh,3),101,120	; 8 - wave, speed .2
		ExtractAnimSeq GetChild(StinkerMesh,3),121,140	; 9 - foottap, speed .15
		ExtractAnimSeq GetChild(StinkerMesh,3),141,160	; 10 - idle, speed .05
		ExtractAnimSeq GetChild(StinkerMesh,3),161,180	; 11 - death, speed .2
		ExtractAnimSeq GetChild(StinkerMesh,3),181,200	; 12 - dance, speed .2
		ExtractAnimSeq GetChild(StinkerMesh,3),201,220	; 13 - sit, speed .2
		ExtractAnimSeq GetChild(StinkerMesh,3),201,218	; 14 - sit, speed not too far back .2
		ExtractAnimSeq GetChild(StinkerMesh,3),109,112	; 15 - constant wave, speed .2
		ExtractAnimSeq GetChild(StinkerMesh,3),221,240	; 16 - use
		ExtractAnimSeq GetChild(StinkerMesh,3),201,220	; 17 - sit, again (for backwards eg after ice)
		ExtractAnimSeq GetChild(StinkerMesh,3),217,219  ; 18 - wee stinker new 1
		ExtractAnimSeq GetChild(StinkerMesh,3),161,178	; 19 - wee stinker freeze
		ExtractAnimSeq GetChild(StinkerMesh,3),108,114	; 20 - wee stinker wave
		ExtractAnimSeq GetChild(StinkerMesh,3),201,219	; 21 - wee stinker sit
		ExtractAnimSeq GetChild(StinkerMesh,3),201,217	; 22 - wee stinker sleep
	EndIf
	HideEntity StinkerMesh

	; StinkerWee
	StinkerWeeMesh=MyLoadMD2("Data/Models/stinkerwee/stinkerwee.md2")
	For i=0 To 2
		StinkerWeeTexture(i)=MyLoadTexture("Data/Models/stinkerwee/stinkerwee"+Str$(i+1)+".jpg",1)
		StinkerWeeTextureSleep(i)=MyLoadTexture("Data/Models/stinkerwee/stinkerwee"+Str$(i+1)+"sleep.jpg",1)
		StinkerWeeTextureSad(i)=MyLoadTexture("Data/Models/stinkerwee/stinkerwee"+Str$(i+1)+"sad.jpg",1)
	Next
	If mode = 0
		EntityTexture StinkerWeeMesh,StinkerWeeTextureSleep(1)
	Else
		EntityTexture StinkerWeeMesh,StinkerWeeTextureSleep(0)
	EndIf
	HideEntity StinkerWeeMesh

	; Cage
	CageMesh=MyLoadMesh("Data/Models/cage/cage.3ds")
	RotateMesh CageMesh,-90,0,0
	CageTexture=MyLoadTexture("Data/Models/cage/cage.jpg",1)
	EntityTexture CageMesh,CageTexture
	HideEntity CageMesh

	; AutoDoor
	AutoDoorMesh=CreateCube()
	ScaleMesh AutoDoorMesh,.47,.47,.47 ; vanilla editor: .5
	PositionMesh AutoDoorMesh,0,.5,0
	AutoDoorTexture=MyLoadTexture("Data/Models/autodoor/autodoor.jpg",1)
	EntityTexture AutoDoorMesh,AutoDoorTexture
	HideEntity AutoDoorMesh

	; StarGate
	StarGateMesh=MyLoadMesh("Data/Models/gate/gate.3ds")
	RotateMesh StarGateMesh,-90,0,0
	HideEntity StarGateMesh

	; Scritters
	ScritterMesh=MyLoadMesh("Data/Models/scritter/scritter.3ds")
	RotateMesh ScritterMesh,-90,0,0
	For i=0 To 6
		ScritterTexture(i)=MyLoadTexture("Data/Models/scritter/scritter"+Str$(i)+".jpg",1)
	Next
	; EntityTexture ScritterMesh,ScritterTexture ; done on object creation
	HideEntity ScritterMesh

	; Star
	StarMesh=MyLoadMesh("Data/Models/star/star.3ds")
	GoldStarTexture=MyLoadTexture("Data/Models/star/goldstar.jpg",1)
	For i=0 To 9
		WispTexture(i)=MyLoadTexture("Data/Models/star/wisp"+Str$(i)+".jpg",1)
	Next
	EntityTexture StarMesh,GoldStarTexture
	HideEntity StarMesh

	; Coin
	CoinMesh=MyLoadMesh("Data/Models/coin/coin.3ds")
	GoldCoinTexture=MyLoadTexture("Data/Models/coin/coin.jpg",1)
	TokenCoinTexture=MyLoadTexture("Data/Models/coin/token.jpg",1)
	EntityTexture CoinMesh,GoldCoinTexture
	HideEntity CoinMesh

	; Key
	KeyMesh=MyLoadMesh("Data/Models/keys/key.3ds")
	HideEntity KeyMesh

	; Signs
	For i=0 To 5
		SignMesh(i)=MyLoadMesh("Data/Models/sign/sign"+Str$(i)+".3ds")
		SignTexture(i)=MyLoadTexture("Data/Models/sign/sign"+Str$(i)+".jpg",1)
		HideEntity SignMesh(i)
	Next

	; Houses
	For i=0 To 2
		DoorTexture(i)=MyLoadTexture("data\models\houses\door"+Str$(i)+".png",1)
	Next
	For i=0 To 1
		CottageTexture(i)=MyLoadTexture("data\models\houses\cottage"+Str$(i)+".png",1)
	Next
	For i=0 To 2
		HouseTexture(i)=MyLoadTexture("data\models\houses\townhouse"+Str$(i)+".png",1)
	Next
	For i=0 To 0
		WindmillTexture(i)=MyLoadTexture("data\models\houses\windmill"+Str$(i)+".png",1)
	Next
	For i=0 To 0
		FenceTexture(i)=MyLoadTexture("data\models\houses\fence"+Str$(i)+".png",1)
	Next

	; Fountain
	Fountain=MyLoadMesh("Data/Models/houses/Fountain01.b3d",0)
	FountainTexture=MyLoadTexture("Data/Models/houses/Fountain01.png",1)
	EntityTexture Fountain,FountainTexture
	HideEntity Fountain

	If mode<>0 And WAEpisode<>1
		PositionEntity cube2,.4,-1,4 ; loading bar: 40%
		RenderWorld
		Flip
	EndIf

	; Gems
	For i=0 To 2
		GemMesh(i)=MyLoadMesh("Data/Models/gems/gem"+Str$(i)+".3ds")
		HideEntity GemMesh(i)
	Next

	; Turtles
	If mode = 0
		TurtleMesh=MyLoadMesh("Data/Models/turtle/dragonturtle2.3ds",0)
		RotateMesh TurtleMesh,-90,0,0
		RotateMesh TurtleMesh,0,90,0
	Else
		TurtleMesh=MyLoadMD2("Data/Models/turtle/dragonturtle.md2")
		ScaleEntity TurtleMesh,.03,.025,.03
	EndIf
	TurtleTexture=MyLoadTexture("Data/Models/turtle/dragonturtle2.png",1)
	LavaTurtleTex=MyLoadTexture("Data/NewModels/Subzero/lavaturtle.png",1)
	RoboTurtleTex=MyLoadTexture("Data/NewModels/Subzero/roboturtle.png",1)
	EntityTexture TurtleMesh,TurtleTexture
	HideEntity TurtleMesh

	; FireFlowers
	FireFlowerMesh=MyLoadMD2("Data/Models/fireflower/fireflower.wdf")
	FireFlowerTexture=MyLoadTexture("Data/Models/fireflower/fireflower04.png",4)
	FireFlowerTexture2=MyLoadTexture("Data/Models/fireflower/fireflowerice.png",1+4)
	EntityTexture FireFlowerMesh,FireFlowerTexture
	HideEntity FireFlowerMesh

	; BurstFlowers
	BurstFlowerMesh=MyLoadMesh("Data/Models/burstflower/burstflower.b3d")
	BurstFlowerTexture=MyLoadTexture("Data/Models/burstflower/burstflower.png",1)
	EntityTexture BurstFlowerMesh,BurstFlowerTexture
	HideEntity BurstFlowerMesh

	; Boxes etc
	BarrelMesh=MyLoadMesh("Data/Models/barrels/barrel.b3d")
	BarrelTexture1=MyLoadTexture("Data/Models/barrels/barrel1.jpg",1)
	BarrelTexture2=MyLoadTexture("Data/Models/barrels/barrel2.jpg",1)
	BarrelTexture3=MyLoadTexture("Data/newmodels/barrels/barrel3.jpg",1)
	BarrelTextureRainbow(0)=MyLoadTexture("Data/newmodels/barrels/barrel00.jpg",1)
	BarrelTextureRainbow(1)=MyLoadTexture("Data/newmodels/barrels/barrel01.jpg",1)
	BarrelTextureRainbow(2)=MyLoadTexture("Data/newmodels/barrels/barrel02.jpg",1)
	BarrelTextureRainbow(3)=MyLoadTexture("Data/newmodels/barrels/barrel03.jpg",1)
	BarrelTextureRainbow(4)=MyLoadTexture("Data/newmodels/barrels/barrel04.jpg",1)
	BarrelTextureRainbow(5)=MyLoadTexture("Data/newmodels/barrels/barrel05.jpg",1)
	BarrelTextureRainbow(6)=MyLoadTexture("Data/newmodels/barrels/barrel06.jpg",1)
	BarrelTextureRainbow(7)=MyLoadTexture("Data/newmodels/barrels/barrel07.jpg",1)
	BarrelTextureRainbow(8)=MyLoadTexture("Data/newmodels/barrels/barrel08.jpg",1)
	NitroGenTex=MyLoadTexture("Data/newmodels/subzero/nitrogen.jpg",1)
	HideEntity BarrelMesh

	; Prism
	PrismMesh=MyLoadMesh("Data/newmodels/retro/prism.3ds")
	PrismTexture=MyLoadTexture("Data/newmodels/retro/prism.jpg",1)
	SuperPrismTexture=MyLoadTexture("Data/newmodels/retro/superprism.jpg",1)
	EntityTexture PrismMesh,PrismTexture
	HideEntity PrismMesh

	; Chompers
	If mode = 0
		ChomperMesh=MyLoadMD2("Data/Models/chomper/chomper.md2")
	Else
		If PortalVersion=0 ; no clue when it isn't 0 but left just in case
			ChomperMesh=MyLoadMD2("Data/Models/chomper/chomper.md2")
		Else
			ChomperMesh=MyLoadMD2("Data/Models/chomper/chomper2.md2")
		EndIf
	EndIf
	ChomperTexture=MyLoadTexture("Data/Models/chomper/chomper.png",1)
	WaterChomperTexture=MyLoadTexture("Data/Models/chomper/wchomper.png",1)
	MechaChomperTexture=MyLoadTexture("Data/Models/chomper/mchomper.png",1)
	EntityTexture ChomperMesh,ChomperTexture
	HideEntity ChomperMesh

	; Bowlers = Spikeyballs
	BowlerMesh=MyLoadMesh("Data/Models/spikyball/spikeyball01.b3d")
	BowlerTexture=MyLoadTexture("Data/Models/spikyball/spikeyball01.png",1)
	EntityTexture BowlerMesh,BowlerTexture
	HideEntity BowlerMesh

	; Busterfly
	BusterflyMesh=MyLoadMD2("Data/Models/busterfly/buster.md2")
	BusterflyTexture=MyLoadTexture("Data/Models/busterfly/buster1.bmp",1+4)
	EntityTexture BusterflyMesh,BusterflyTexture
	HideEntity BusterflyMesh

	; RubberDucky
	RubberDuckyMesh=MyLoadMesh("Data/Models/rubberducky/rubberducky.b3d")
	RubberDuckyTexture=MyLoadTexture("Data/Models/rubberducky/rubberducky.png",1)
	EntityTexture RubberDuckyMesh,RubberDuckyTexture
	HideEntity RubberDuckyMesh

	; Thwarts
	ThwartMesh=MyLoadMD2("Data/Models/thwart/thwart05.md2")
	For i=0 To 7
		ThwartTexture(i)=MyLoadTexture("Data/Models/thwart/thwart"+Str$(i)+".jpg",1)
	Next
	EntityTexture ThwartMesh,ThwartTexture(0)
	HideEntity ThwartMesh

	; Tentacle
	TentacleTexture=MyLoadTexture("Data/Models/trees/tentacle.jpg",1)
	TentacleReverseTexture=MyLoadTexture("Data/NewModels/trees/tentacle2.jpg",1)
	TentacleMesh=MyLoadAnimMesh("Data/Models/trees/tentacle.b3d")
	ExtractAnimSeq GetChild(TentacleMesh,3),41,60
	For i=1 To CountChildren(TentacleMesh)
		EntityTexture GetChild(TentacleMesh,i),TentacleTexture
	Next
	HideEntity TentacleMesh

	; Crabs
	CrabMesh=MyLoadMD2("Data/Models/crab/crab.md2")
	CrabTexture1=MyLoadTexture("Data/Models/crab/crab03a.jpg",1)
	CrabTexture2=MyLoadTexture("Data/Models/crab/crab03b.jpg",1)
	IceCrabTex=MyLoadTexture("Data/NewModels/SubZero/icecrab.jpg",1)
	EntityTexture CrabMesh,CrabTexture1
	HideEntity CrabMesh

	; Ice Troll
	TrollMesh=MyLoadMD2("Data/Models/thwart/ice troll.md2")
	TrollTexture=MyLoadTexture("Data/Models/thwart/icetroll01.bmp",1)
	EntityTexture TrollMesh,TrollTexture
	HideEntity TrollMesh

	If mode<>0 And WAEpisode<>1
		PositionEntity cube2,.5,-1,4 ; loading bar: 50%
		RenderWorld
		Flip
	EndIf

	; Kaboom
	KaboomMesh=MyLoadMD2("Data/Models/kaboom/kaboom.md2")
	For i=0 To 4
		KaboomTexture(i)=MyLoadTexture("Data/Models/kaboom/kaboom0"+Str$(i+1)+".jpg",1)	
	Next
	EntityTexture KaboomMesh,KaboomTexture(0)
	KaboomTextureSquint=MyLoadTexture("Data/Models/kaboom/kaboom00_squint.bmp",1)
	HideEntity KaboomMesh
	
	KaboomRTWMesh=myLoadMD2("data\newmodels\bomb\bomb.md2")
	KaboomRTWTexture=myLoadTexture("data\newmodels\bomb\bomb.bmp",1)
	EntityTexture KaboomRTWMesh,KaboomRTWTexture
	HideEntity KaboomRTWMesh

	; Retrostuff
	RetroBoxMesh=MyLoadMesh("Data/Models/retro/box.3ds")
	RetroBoxTexture=MyLoadTexture("Data/Models/retro/woodbox.bmp",1)
	EntityTexture RetroBoxMesh,RetroBoxTexture
	HideEntity RetroBoxMesh
	RetroCoilyMesh=MyLoadMD2("Data/Models/retro/coily.md2")
	RetroCoilyTexture=MyLoadTexture("Data/Models/retro/coily.bmp",1)
	EntityTexture RetroCoilyMesh,RetroCoilyTexture
	HideEntity RetroCoilyMesh
	RetroScougeMesh=MyLoadMesh("Data/Models/retro/scouge.3ds")
	RetroScougeTexture=MyLoadTexture("Data/Models/retro/scouge3.bmp",1)
	RetroScougeTexture2=MyLoadTexture("Data/NewModels/retro/scouge4.bmp",1)
	EntityTexture RetroScougeMesh,RetroScougeTexture
	RotateMesh RetroScougeMesh,-90,0,0
	RotateMesh RetroScougeMesh,0,-90,0
	HideEntity RetroScougeMesh
	RetroUfoMesh=MyLoadMesh("Data/Models/retro/ufo.3ds")
	RetroUfoTexture=MyLoadTexture("Data/Models/retro/ufo.bmp",1)
	EntityTexture RetroUfoMesh,RetroUfoTexture
	RotateMesh RetroUfoMesh,-90,0,0
	RotateMesh RetroUfoMesh,0,-90,0
	HideEntity RetroUfoMesh
	RetroZBotMesh=MyLoadMesh("Data/Models/retro/zbot.3ds")
	RetroZBotTexture=MyLoadTexture("Data/Models/retro/zbot.bmp",1)
	EntityTexture RetroZBotMesh,RetroZBotTexture
	RotateMesh RetroZBotMesh,-90,0,0
	RotateMesh RetroZBotMesh,0,90,0
	HideEntity RetroZBotMesh
	RetroRainbowCoinTexture=MyLoadTexture("Data/Models/retro/rainbowcoin.bmp",1)

	; Zbots
	WeeBotMesh=MyLoadMesh("Data/Models/weebot/weebot.3ds")
	WeeBotTexture=MyLoadTexture("Data/Models/weebot/weebot.jpg",1)
	EntityTexture WeeBotMesh,WeeBotTexture
	RotateMesh WeeBotMesh,-90,0,0
	RotateMesh WeeBotMesh,0,180,0
	HideEntity WeeBotMesh
	ZapbotMesh=MyLoadMesh("Data/Models/zapbot/zapbot.3ds")
	ZapbotTexture=MyLoadTexture("Data/Models/zapbot/zapbot.jpg",1)
	EntityTexture ZapbotMesh,ZapbotTexture
	RotateMesh ZapbotMesh,-90,0,0
	RotateMesh ZapbotMesh,0,180,0
	HideEntity ZapbotMesh

	ZBotNPCMesh=MyLoadMesh("Data/Models/zbots/zbotnpc.3ds")
	RotateMesh ZBotNPCMesh,-90,0,0
	RotateMesh ZBotNPCMesh,0,90,0
	ScaleMesh ZBotNPCMesh,1,1.5,1
	For i=0 To 7
		ZBotNPCTexture(i)=MyLoadTexture("Data/Models/zbots/zbotnpc.jpg",1)
	Next
	EntityTexture ZBotNPCMesh,ZBotNPCTexture(0)
	HideEntity ZBotNPCMesh

	MothershipMesh=MyLoadMesh("Data/Models/other/starship.3ds")
	MothershipTexture=MyLoadTexture("Data/Models/other/mothership.jpg",1)
	EntityTexture MothershipMesh,MothershipTexture
	HideEntity MothershipMesh

	; Portal
	PortalWarpMesh=CreateCylinder()
	RotateMesh PortalWarpMesh,-90,0,0
	ScaleMesh PortalWarpMesh,2,2,4.5*1.15
	HideEntity PortalWarpMesh
	StarTexture=MyLoadTexture("Data/Graphics/stars.jpg",1)
	RainbowTexture=MyLoadTexture("Data/Graphics/rainbow.jpg",1)
	RainbowTexture2=MyLoadTexture("Data/Graphics/rainbow.jpg",1)

	; Lurker
	LurkerMesh=MyLoadMesh("Data/Models/lurker/lurker.3ds")
	LurkerTexture=MyLoadTexture("Data/Models/lurker/lurker.jpg",1)
	EntityTexture LurkerMesh,LurkerTexture
	RotateMesh LurkerMesh,-90,0,0
	HideEntity LurkerMesh

	; Ghosts
	GhostMesh=MyLoadMesh("Data/Models/ghost/ghost.3ds")
	GhostTexture=MyLoadTexture("Data/Models/ghost/ghost.jpg",1)
	RotateMesh GhostMesh,-90,0,0
	RotateMesh GhostMesh,0,180,0
	EntityTexture GhostMesh,GhostTexture
	HideEntity GhostMesh

	WraithMesh=MyLoadMesh("Data/Models/ghost/wraith.3ds")
	RotateMesh WraithMesh,-90,0,0
	RotateMesh WraithMesh,0,180,0
	For i=0 To 2
		WraithTexture(i)=MyLoadTexture("Data/Models/ghost/wraith"+Str$(i)+".jpg",1)
	Next
	For i=0 To 7
		NewWraithTexture(i)=MyLoadTexture("Data/NewModels/newwraith/wraith"+Str$(i)+".jpg",1)
	Next
	EntityTexture WraithMesh,WraithTexture(0)
	HideEntity WraithMesh

	; Magic Turret
	MagicTurretMesh=MyLoadMesh("Data/newmodels/turret/turret.b3d")
	ScaleMesh MagicTurretMesh,0.035,0.035,0.035
	MagicTurretTexture=MyLoadTexture("Data/newmodels/turret/turret.jpg",1)
	EntityTexture MagicTurretMesh, MagicTurretTexture
	HideEntity MagicTurretMesh

	If mode<>0 And WAEpisode<>1
		PositionEntity cube2,.6,-1,4 ; loading bar: 60%
		RenderWorld
		Flip
	EndIf
	
	; Vehicle
	VehicleTexture=MyLoadTexture("Data/newmodels/vehicle/model.png",1)
	GVehicleTexture=MyLoadTexture("Data/newmodels/vehicle/ghost.png",1)
	VDeviceTexture=MyLoadTexture("Data/newmodels/vehicle/arrow.png",1)
	VDeviceTexture2=MyLoadTexture("Data/newmodels/vehicle/arrow2.png",1)
	
	VRotatorDefault=MyLoadTexture("Data/newmodels/vehicle/rotate.png",1)
	VRotateTexture=MyLoadTexture("Data/newmodels/vehicle/rotator1.png",1)
	VRotateTexture2=MyLoadTexture("Data/newmodels/vehicle/rotator2.png",1)
	VRotateTexture3=MyLoadTexture("Data/newmodels/vehicle/rotator3.png",1)
	
	VehicleGen1=MyLoadTexture("Data/newmodels/vehicle/gen1.png",1)
	VehicleGen2=MyLoadTexture("Data/newmodels/vehicle/gen2.png",1)
	VehicleGen3=MyLoadTexture("Data/newmodels/vehicle/gen3.png",1)
	VehicleGen4=MyLoadTexture("Data/newmodels/vehicle/gen4.png",1)
	
	VDestroyer=MyLoadTexture("Data/newmodels/vehicle/THE_DESTROYER.png",1)

	; Obstacles
	ObstacleMesh(1)=MyLoadMesh("Data/Models/Trees/rock1.3ds")
	ObstacleTexture(1)=MyLoadTexture("Data/Models/Trees/rocks.jpg",1)
	EntityTexture ObstacleMesh(1),ObstacleTexture(1)

	ObstacleMesh(2)=MyLoadMesh("Data/Models/Trees/rock2.3ds")
	ObstacleTexture(2)=MyLoadTexture("Data/Models/Trees/rocks2.jpg",1)
	EntityTexture ObstacleMesh(2),ObstacleTexture(2)

	ObstacleMesh(3)=MyLoadMesh("Data/Models/Other/volcano01.b3d")
	ObstacleTexture(3)=MyLoadTexture("Data/Models/Other/volcano01.bmp",1)
	EntityTexture ObstacleMesh(3),ObstacleTexture(3)

	ObstacleMesh(4)=MyLoadMesh("Data/Models/Other/volcano01.b3d")
	ObstacleTexture(4)=MyLoadTexture("Data/Models/other/volcano02.jpg",1)
	EntityTexture ObstacleMesh(4),ObstacleTexture(4)

	ObstacleMesh(5)=MyLoadMesh("Data/Models/Trees/flower.3ds")
	ObstacleTexture(5)=MyLoadTexture("Data/Models/Trees/flower1.jpg",1)
	EntityTexture ObstacleMesh(5),ObstacleTexture(5)

	ObstacleMesh(6)=MyLoadMesh("Data/Models/Trees/flower2.3ds")
	ObstacleTexture(6)=MyLoadTexture("Data/Models/Trees/flower2.bmp",1)
	EntityTexture ObstacleMesh(6),ObstacleTexture(6)
	; UpdateNormals ObstacleMesh(6)

	ObstacleMesh(7)=MyLoadMesh("Data/Models/Trees/watervine.b3d")
	ObstacleTexture(7)=MyLoadTexture("Data/Models/Trees/watervine.jpg",1)
	EntityTexture ObstacleMesh(7),ObstacleTexture(7)
	; UpdateNormals ObstacleMesh(7)

	ObstacleMesh(8)=MyLoadMesh("Data/Models/Trees/fern.b3d")
	ObstacleTexture(8)=MyLoadTexture("Data/Models/Trees/fern.bmp",1+4)
	EntityTexture ObstacleMesh(8),ObstacleTexture(8)

	ObstacleMesh(9)=MyLoadMesh("Data/Models/Trees/fern02.b3d")
	ObstacleTexture(9)=MyLoadTexture("Data/Models/Trees/fern.bmp",1+4)
	EntityTexture ObstacleMesh(9),ObstacleTexture(9)

	ObstacleMesh(10)=MyLoadMesh("Data/Models/Trees/mushroom.3ds")
	MushroomTex(0)=MyLoadTexture("Data/Models/Trees/mushroom.jpg",1)
	MushroomTex(1)=MyLoadTexture("Data/Models/Trees/mushroom2.jpg",1)
	MushroomTex(2)=MyLoadTexture("Data/Models/Trees/mushroom3.jpg",1)

	ObstacleMesh(11)=MyLoadMesh("Data/Models/Trees/fern3.3ds")
	ObstacleTexture(11)=MyLoadTexture("Data/Models/Trees/fern3.png",1+4)
	EntityTexture ObstacleMesh(11),ObstacleTexture(11)

	ObstacleMesh(12)=MyLoadMesh("Data/Models/Trees/plant1.3ds")
	ObstacleTexture(12)=MyLoadTexture("Data/Models/Trees/plant1.png",1+4)
	EntityTexture ObstacleMesh(12),ObstacleTexture(12)

	ObstacleMesh(13)=MyLoadMesh("Data/Models/Trees/plant2.b3d")
	ObstacleTexture(13)=MyLoadTexture("Data/Models/Trees/plant2.png",1+4)
	EntityTexture ObstacleMesh(13),ObstacleTexture(13)

	ObstacleMesh(15)=MyLoadMesh("Data/Models/Trees/leaftree01.b3d")
	ObstacleTexture(15)=MyLoadTexture("Data/Models/Trees/leaftree01_03.png",1+4)
	EntityTexture ObstacleMesh(15),ObstacleTexture(15)

	ObstacleMesh(16)=MyLoadMesh("Data/Models/Trees/evergreentree01.b3d")
	ObstacleTexture(16)=MyLoadTexture("Data/Models/Trees/evergreen_01.png",1+4)
	EntityTexture ObstacleMesh(16),ObstacleTexture(16)

	ObstacleMesh(17)=MyLoadMesh("Data/Models/Trees/evergreentree01.b3d")
	ObstacleTexture(17)=MyLoadTexture("Data/Models/Trees/evergreen_02.png",1+4)
	EntityTexture ObstacleMesh(17),ObstacleTexture(17)

	ObstacleMesh(18)=MyLoadMesh("Data/Models/Trees/leaftree01.b3d")
	ObstacleTexture(18)=MyLoadTexture("Data/Models/Trees/leaftree01_02.png",1+4)
	EntityTexture ObstacleMesh(18),ObstacleTexture(18)

	ObstacleMesh(19)=MyLoadMesh("Data/Models/Trees/leaftree01.b3d")
	ObstacleTexture(19)=MyLoadTexture("Data/Models/Trees/leaftree01_01.png",1+4)
	EntityTexture ObstacleMesh(19),ObstacleTexture(19)

	ObstacleMesh(20)=MyLoadMesh("Data/Models/Trees/leaftree02.b3d")
	ObstacleTexture(20)=MyLoadTexture("Data/Models/Trees/leaftree02_01.png",1+4)
	EntityTexture ObstacleMesh(20),ObstacleTexture(20)

	ObstacleMesh(21)=MyLoadMesh("Data/Models/Trees/leaftree02.b3d")
	ObstacleTexture(21)=MyLoadTexture("Data/Models/Trees/leaftree02_02.png",1+4)
	EntityTexture ObstacleMesh(21),ObstacleTexture(21)

	ObstacleMesh(22)=MyLoadMesh("Data/Models/Trees/tree_jungle_typeA.b3d")
	ObstacleTexture(22)=MyLoadTexture("Data/Models/Trees/tree_jungle_typeA.bmp",1+4)
	EntityTexture ObstacleMesh(22),ObstacleTexture(22)

	ObstacleMesh(23)=MyLoadMesh("Data/Models/Trees/tree_jungle_typeB.b3d")
	ObstacleTexture(23)=MyLoadTexture("Data/Models/Trees/tree_jungle_typeB.bmp",1+4)
	EntityTexture ObstacleMesh(23),ObstacleTexture(23)

	ObstacleMesh(24)=MyLoadMesh("Data/Models/Trees/tree_palm.b3d")
	ObstacleTexture(24)=MyLoadTexture("Data/Models/Trees/palmtree01.bmp",1+4)
	EntityTexture ObstacleMesh(24),ObstacleTexture(24)

	ObstacleMesh(25)=MyLoadMesh("Data/Models/Bridges/bridgeend.3ds")
	ObstacleTexture(25)=MyLoadTexture("Data/Models/Bridges/bridgebrick.png",1)
	EntityTexture ObstacleMesh(25),ObstacleTexture(25)

	ObstacleMesh(26)=MyLoadMesh("Data/Models/houses/canopy.3ds")
	ObstacleTexture(26)=MyLoadTexture("Data/Models/houses/canopy.jpg",1)
	EntityTexture ObstacleMesh(26),ObstacleTexture(26)

	ObstacleMesh(27)=MyLoadMesh("Data/Models/houses/streetlight01.b3d")
	ObstacleTexture(27)=MyLoadTexture("Data/Models/houses/streetlight02.png",1+4)
	EntityTexture ObstacleMesh(27),ObstacleTexture(27)

	ObstacleMesh(28)=MyLoadMesh("Data/Models/houses/pillar.3ds")
	ObstacleTexture(28)=MyLoadTexture("Data/Models/houses/pillar1.jpg",1)
	EntityTexture ObstacleMesh(28),ObstacleTexture(28)

	ObstacleMesh(29)=MyLoadMesh("Data/Models/ladder/ladder.3ds")
	ObstacleTexture(29)=MyLoadTexture("Data/Models/houses/pillar1.jpg",1)
	EntityTexture ObstacleMesh(29),ObstacleTexture(29)

	ObstacleMesh(30)=MyLoadMesh("Data/Models/furniture/table.3ds")
	ObstacleTexture(30)=MyLoadTexture("Data/Models/furniture/table.jpg",1)
	EntityTexture ObstacleMesh(30),ObstacleTexture(30)

	ObstacleMesh(31)=MyLoadMesh("Data/Models/furniture/chair.3ds")
	ObstacleTexture(31)=MyLoadTexture("Data/Models/furniture/chair.jpg",1)
	EntityTexture ObstacleMesh(31),ObstacleTexture(31)

	ObstacleMesh(32)=MyLoadMesh("Data/Models/furniture/bed.3ds")
	ObstacleTexture(32)=MyLoadTexture("Data/Models/furniture/bed.jpg",1)
	EntityTexture ObstacleMesh(32),ObstacleTexture(32)

	ObstacleMesh(33)=MyLoadMesh("Data/Models/furniture/bookshelf01.b3d")
	ObstacleTexture(33)=MyLoadTexture("Data/Models/furniture/bookshelf01.png",1)
	EntityTexture ObstacleMesh(33),ObstacleTexture(33)

	ObstacleMesh(34)=MyLoadMesh("Data/Models/furniture/arcade.3ds")
	ObstacleTexture(34)=MyLoadTexture("Data/Models/furniture/arcade.jpg",1)
	EntityTexture ObstacleMesh(34),ObstacleTexture(34)

	ObstacleMesh(35)=MyLoadMesh("Data/Models/houses/pyramid.3ds")
	ObstacleTexture(35)=MyLoadTexture("Data/Models/houses/pyramid.jpg",1)
	EntityTexture ObstacleMesh(35),ObstacleTexture(35)

	ObstacleMesh(36)=MyLoadMesh("Data/Models/houses/Cottage.b3d")

	ObstacleMesh(37)=MyLoadMesh("Data/Models/houses/townhouse_01a.b3d")

	ObstacleMesh(38)=MyLoadMesh("Data/Models/houses/townhouse_01b.b3d")

	ObstacleMesh(39)=MyLoadMesh("Data/Models/houses/townhouse_02a.b3d")

	ObstacleMesh(40)=MyLoadMesh("Data/Models/houses/townhouse_02b.b3d")

	ObstacleMesh(41)=MyLoadMesh("Data/Models/houses/windmill_main.b3d")

	ObstacleMesh(42)=MyLoadMesh("Data/Models/houses/windmill_rotor.b3d")
	PositionMesh ObstacleMesh(42),0,-5.65/.037,1.25/.037 ; vanilla editor doesn't do that

	ObstacleMesh(43)=MyLoadMesh("Data/Models/houses/hut01.b3d")
	ObstacleTexture(43)=MyLoadTexture("Data/Models/houses/hut01.jpg",1+4)
	EntityTexture ObstacleMesh(43),ObstacleTexture(43)

	ObstacleMesh(44)=MyLoadMesh("Data/Models/other/ship01.b3d")
	ObstacleTexture(44)=MyLoadTexture("Data/Models/other/ship01.bmp",1)
	EntityTexture ObstacleMesh(44),ObstacleTexture(44)

	ObstacleMesh(45)=MyLoadMesh("Data/Models/houses/waterwheel.3ds")
	ObstacleTexture(45)=MyLoadTexture("Data/Models/cage/cage.jpg",1)
	EntityTexture ObstacleMesh(45),ObstacleTexture(45)

	ObstacleMesh(46)=MyLoadMesh("Data/Models/houses/bridge.3ds")
	ObstacleTexture(46)=MyLoadTexture("Data/Models/cage/cage.jpg",1)
	EntityTexture ObstacleMesh(46),ObstacleTexture(46)

	ObstacleMesh(47)=MyLoadMesh("Data/Models/houses/machine.3ds")
	ObstacleTexture(47)=MyLoadTexture("Data/Models/houses/machine.jpg",1)
	EntityTexture ObstacleMesh(47),ObstacleTexture(47)

	ObstacleMesh(48)=MyLoadMesh("Data/Models/other/starship.3ds")
	ObstacleTexture(48)=MyLoadTexture("Data/Models/other/starship.jpg",1)
	EntityTexture ObstacleMesh(48),ObstacleTexture(48)

	ObstacleMesh(50)=MyLoadMesh("Data/Models/portal/portal.3ds")
	RotateMesh ObstacleMesh(50),-90,0,0
	ObstacleTexture(50)=MyLoadTexture("Data/Models/portal/portal2.jpg",1)
	EntityTexture ObstacleMesh(50),ObstacleTexture(50)

	ObstacleMesh(51)=MyLoadMesh("Data/Models/newobstacles/cactus1.b3d")
	ObstacleTexture(51)=MyLoadTexture("Data/Models/newobstacles/cactus1.png",1+4)
	EntityTexture ObstacleMesh(51),ObstacleTexture(51)
	FlipMesh ObstacleMesh(51)

	ObstacleMesh(52)=MyLoadMesh("Data/Models/newobstacles/cactus2.b3d")
	ObstacleTexture(52)=MyLoadTexture("Data/Models/newobstacles/cactus2.png",1+4)
	EntityTexture ObstacleMesh(52),ObstacleTexture(51)

	ObstacleMesh(53)=MyLoadMesh("Data/Models/newobstacles/cactus3.b3d")
	ObstacleTexture(53)=MyLoadTexture("Data/Models/newobstacles/cactus3.png",1+4)
	EntityTexture ObstacleMesh(53),ObstacleTexture(51)

	ObstacleMesh(54)=MyLoadMesh("Data/Models/newobstacles/cactus4.b3d")
	ObstacleTexture(54)=MyLoadTexture("Data/Models/newobstacles/cactus4.png",1+4)
	EntityTexture ObstacleMesh(54),ObstacleTexture(51)

	ObstacleMesh(55)=MyLoadMesh("Data/Models/newobstacles/leafy1.b3d")
	ObstacleTexture(55)=MyLoadTexture("Data/Models/newobstacles/leafy1.png",1+4)
	EntityTexture ObstacleMesh(55),ObstacleTexture(55)

	ObstacleMesh(56)=MyLoadMesh("Data/Models/newobstacles/leafy2.b3d")
	ObstacleTexture(56)=MyLoadTexture("Data/Models/newobstacles/leafy2.png",1+4)
	EntityTexture ObstacleMesh(56),ObstacleTexture(55)

	ObstacleMesh(57)=MyLoadMesh("Data/Models/newobstacles/leafy3.b3d")
	ObstacleTexture(57)=MyLoadTexture("Data/Models/newobstacles/leafy3.png",1+4)
	EntityTexture ObstacleMesh(57),ObstacleTexture(55)

	ObstacleMesh(58)=MyLoadMesh("Data/Models/newobstacles/leafy4.b3d")
	ObstacleTexture(58)=MyLoadTexture("Data/Models/newobstacles/leafy4.png",1+4)
	EntityTexture ObstacleMesh(58),ObstacleTexture(55)

	ObstacleMesh(59)=MyLoadMesh("Data/Models/newobstacles/rock1.b3d")
	ObstacleTexture(59)=MyLoadTexture("Data/Models/newobstacles/rock1.png",1)
	EntityTexture ObstacleMesh(59),ObstacleTexture(59)

	ObstacleMesh(60)=MyLoadMesh("Data/Models/newobstacles/rock2.b3d")
	ObstacleTexture(60)=MyLoadTexture("Data/Models/newobstacles/rock2.png",1)
	EntityTexture ObstacleMesh(60),ObstacleTexture(59)

	ObstacleMesh(61)=MyLoadMesh("Data/Models/newobstacles/rock3.b3d")
	ObstacleTexture(61)=MyLoadTexture("Data/Models/newobstacles/rock3.png",1)
	EntityTexture ObstacleMesh(61),ObstacleTexture(59)

	ObstacleMesh(62)=MyLoadMesh("Data/Models/newobstacles/rock4.b3d")
	ObstacleTexture(62)=MyLoadTexture("Data/Models/newobstacles/rock4.png",1)
	EntityTexture ObstacleMesh(62),ObstacleTexture(59)

	For i=1 To 62
		Select i
			Case 14,49
				; no obstacles 14 or 49
			Default
				HideEntity ObstacleMesh(i)
		End Select
	Next

	; V1.04 Preloads
	SteppingStoneCylinder=MyLoadMesh("Data/Models/bridges/Cylinder1.b3d");MyCreateCylinder()
	; ScaleMesh Cylinder,0.497,0.497,0.497 ; vanilla cylinder1.b3d is slightly smaller than 1x1x1
	; PositionMesh Cylinder,0,0.5,0
	HideEntity SteppingStoneCylinder
	Fence1=MyLoadMesh("Data/Models/houses/fence.3ds",0)
	HideEntity Fence1
	Fence2=MyLoadMesh("Data/Models/houses/fenceb.b3d",0)
	HideEntity Fence2
	FencePost=MyLoadMesh("Data/Models/houses/fence_post.3ds",0)
	HideEntity FencePost
	Door01b3d=MyLoadMesh("Data/Models/houses/door01.b3d",0)
	HideEntity Door01b3d
	Door013ds=MyLoadMesh("Data/Models/houses/door01.3ds",0)
	HideEntity Door013ds
	Square=MyLoadMesh("Data/Models/Squares/Square1.b3d",0)
	HideEntity Square
	Townhouse01a=MyLoadMesh("Data/Models/Houses/townhouse_01a.b3d")
	HideEntity Townhouse01a
	Townhouse01b=MyLoadMesh("Data/Models/Houses/townhouse_01b.b3d")
	HideEntity Townhouse01b
	Townhouse02a=MyLoadMesh("Data/Models/Houses/townhouse_02a.b3d")
	HideEntity Townhouse02a
	Townhouse02b=MyLoadMesh("Data/Models/Houses/townhouse_02b.b3d")
	HideEntity Townhouse02b
	Cottage=MyLoadMesh("Data/Models/Houses/Cottage.b3d")
	HideEntity Cottage
	
	ReloadColorTextures() ; bugs out if not done now
End Function

Function ReloadColorTextures()
	FreeTexture ButtonTexture
	ButtonTexture=MyLoadTexture("Data/Graphics/buttons"+Str$(GateKeyVersion)+".bmp",1+4)
	
	buttonImage=CreateImage(TextureWidth(ButtonTexture),TextureHeight(ButtonTexture))
	CopyRect 0,0,TextureWidth(ButtonTexture),TextureHeight(ButtonTexture),0,0,TextureBuffer(ButtonTexture),ImageBuffer(buttonImage)
	For i=0 To 63
		FreeTexture ColorTexture(i)
		ColorTexture(i)=CreateTexture(64,64,1)
		CopyRect 64*(i Mod 8),256+64*(Floor(i/8)),64,64,0,0,ImageBuffer(buttonImage),TextureBuffer(ColorTexture(i))
	Next
	FreeImage buttonImage

	FreeTexture GateTexture
	GateTexture=MyLoadTexture("Data/Graphics/gates"+Str$(GateKeyVersion)+".bmp",1)
	
	For j=0 To 7
		FreeTexture LogicGates(j)
	Next
	
	LogicGates(0)=MyLoadTexture("Data/NewModels/Logic/not"+Str$(GateKeyVersion)+".bmp",1)
	
	LogicGates(1)=MyLoadTexture("Data/NewModels/Logic/and"+Str$(GateKeyVersion)+".bmp",1)
	LogicGates(2)=MyLoadTexture("Data/NewModels/Logic/or"+Str$(GateKeyVersion)+".bmp",1)
	LogicGates(3)=MyLoadTexture("Data/NewModels/Logic/xor"+Str$(GateKeyVersion)+".bmp",1)
	
	LogicGates(4)=MyLoadTexture("Data/NewModels/Logic/nand"+Str$(GateKeyVersion)+".bmp",1)
	LogicGates(5)=MyLoadTexture("Data/NewModels/Logic/nor"+Str$(GateKeyVersion)+".bmp",1)
	LogicGates(6)=MyLoadTexture("Data/NewModels/Logic/xnor"+Str$(GateKeyVersion)+".bmp",1)

	FreeTexture TransporterTexture
	TransporterTexture=MyLoadTexture("Data/NewGraphics/transporter"+Str$(GateKeyVersion)+".png",1+4)
End Function

Function GetColorTexture(col,d=64)
	i=Abs(col) Mod d
	If col<0
		i=d-i
		If i=d
			i=0
		EndIf
	EndIf
	Return ColorTexture(i)
End Function

Function CustomStuffCleanup()
	For i=0 To NofCustomMeshes-1
		CustomModelName$(i)=""
		FreeEntity CustomModelMesh(i)
		CustomModelMesh(i)=0
	Next
	NofCustomMeshes=0
	For i=0 To NofCustomTextures-1
		CustomTextureName$(i)=""
		FreeTexture CustomTexture(i)
		CustomTexture(i)=0
		FreeTexture CustomTextureMask(i)
		CustomTextureMask(i)=0
	Next
	NofCustomTextures=0
End Function