; Specific Textures
; ----------------
Global BlankTexture
; Buttons
Global ButtonTexture
; Gates
Global GateTexture
Global CloudTexture

Dim LogicGates(7)

; ColorTexture ; NEW!
Dim ColorTexture(63)
Dim KeyCardTexture(1)
; IconTexture
Global IconTextureCustom=0
; Teleporters ; REVISIT! Change to Global (stars) and apply it over
Global TeleporterTexture; org=16
Dim OldTeleporterTexture(16)
Global TransporterTexture
Global StepStoneTexture
; FireTrap
Global FireTrapTexture

; Spring
Global SpringTexture

; Void
Global VoidTexture

; 1.04 preloads
Global Square,GloveTex

; GrowFlowers, Floingbubbles
Global PlasmaTexture,FloingTexture,Flashbubble

; Pushbot
Global PushbotTexture
Global KillerMoobot

; Mirror
Dim MirrorTexture(5) ; org=6
Global SkyMachineMapTexture

; NEW!
; Global PortalGloveTex
; Dim PortalTexture(1), FlipButtonTexture(3)

; Specific Models
; ----------------
; Stinkers
Global StinkerMesh
Dim StinkerTexture(8,5) ; org=100x10
Global StinkerSmokedTexture

; Wee Stinkers
Global StinkerWeeMesh
Dim StinkerWeeTexture(2),StinkerWeeTextureSleep(2),StinkerWeeTextureSad(2)

; Cages
Global CageMesh,CageTexture

; AutoDoors
Global AutoDoorMesh,AutoDoorTexture

; StarGates
Global StarGateMesh

; Scritters
Global ScritterMesh
Dim ScritterTexture(6)

; SteppingStones
Dim SteppingStoneTexture(3)

; WaterFall
; Global WaterFallMesh ; mesh generated with a function
Dim WaterFallTexture(2)

; Star
Global StarMesh,GoldStarTexture
Dim WispTexture(9) ; org=10

; Coin
Global CoinMesh,GoldCoinTexture,TokenCoinTexture

; Key
Global KeyMesh

; Signs
Dim SignMesh(5),SignTexture(5)

; Houses
Dim DoorTexture(10),CottageTexture(10),HouseTexture(10),OldWindmillTexture(10),OldFenceTexture(10)
Global WindmillTexture,FenceTexture ; there's only one of each
; Fountain
Global Fountain,FountainTexture

; Shadows
Global ShadowTexture ; org=10 but there's only one texture
; Gems
Dim GemMesh(2) ; org=10

; Turtles
Global TurtleMesh,TurtleTexture

; FireFlowers
Global FireFlowerMesh,FireFlowerTexture,FireFlowerTexture2

; BurstFlowers
Global BurstFlowerMesh,BurstFlowerTexture

; Boxes etc
Global BarrelMesh,BarrelTexture1,BarrelTexture2,BarrelTexture3,PrismMesh,PrismTexture,SuperPrismTexture
Dim BarrelTextureRainbow(9)

; Chompers
Global ChomperMesh,ChomperTexture,WaterChomperTexture,MechaChomperTexture

; Bowlers = Spikeyballs
Global BowlerMesh,BowlerTexture

; Busterflies
Global BusterflyMesh,BusterflyTexture

; RubberDucky
Global RubberDuckyMesh,RubberDuckyTexture

; Thwarts
Global ThwartMesh
Dim ThwartTexture(8) ; org=9

; Tentacles
Global TentacleMesh,TentacleTexture,TentacleReverseTexture

; Crabs
Global CrabMesh, CrabTexture1, CrabTexture2

; Trolls
Global TrollMesh, TrollTexture

; Kabooms
Global KaboomMesh
Dim KaboomTexture(4)
Global KaboomTextureSquint

Global KaboomRTWMesh, KaboomRTWTexture

; Retrostuff
Global RetroBoxMesh,RetroBoxTexture,RetroCoilyMesh,RetroCoilyTexture,RetroScougeMesh,RetroScougeTexture,RetroScougeTexture2
Global RetroUfoMesh,RetroUfoTexture,RetroZBotMesh,RetroZBotTexture,RetroRainbowCoinTexture
Global WeeBotMesh,WeeBotTexture
Global ZapbotMesh,ZapbotTexture

; ZBots
; Global PushbotMesh,PushbotTexture ; mesh unused, texture moved to texture section above
Global ZBotNPCMesh
Dim ZBotNPCTexture(7) ; org=8
Global MothershipMesh, MothershipTexture
Global PortalWarpMesh,StarTexture,RainbowTexture,RainbowTexture2

; Lurker
Global LurkerMesh,LurkerTexture

; Ghosts
Global GhostMesh,WraithMesh,GhostTexture
Dim WraithTexture(2) ; org=3
Dim NewWraithTexture(7)

; Magic Turret
Global MagicTurretMesh, MagicTurretTexture

; Vehicle
Global VehicleTexture, VDeviceTexture, VDeviceTexture2
Global VRotatorDefault, VRotateTexture, VRotateTexture2, VRotateTexture3
Global VehicleGen1, VehicleGen2, VehicleGen3, VehicleGen4, VDestroyer
Global GVehicleTexture

; Obstacles
Dim ObstacleMesh(63),ObstacleTexture(63) ; org=100x100
Dim MushroomTex(3)

; V1.04 Preloads
Global SteppingStoneCylinder ; shouldn't be required anymore
Global Fence1,Fence2,FencePost,Door01b3d,Door013ds
Global Townhouse01a,Townhouse01b,Townhouse02a,Townhouse02b,Cottage

; WA Lattius Road SubZero
Global IceCrabTex
Global NitrogenTex
Global LavaTurtleTex, RoboTurtleTex

; NEW! Custom Models and Textures are now held in memory so we don't duplicate them, also there's a limit of 50 per .wlv
Dim CustomModelName$(100),CustomModelMesh(100),CustomTextureName$(100),CustomTexture(100),CustomTextureMask(100)
Global NofCustomMeshes=0, NofCustomTextures=0