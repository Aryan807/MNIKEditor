; 2D
; ============

Function MyLoadImage(src$)
	out$ = EncryptedFileName(src) + ".wdf"
	result = LoadImage(out)
	If result=0
		result=LoadImage(src)
		If result=0
			FileNotFoundError("Couldn't load Image: " + src, src, out)
		EndIf
	EndIf
	Return result
End Function

; 3D
; ============

Function MyLoadTexture(src$,flag=1)
	out$ = OverideGameData(EncryptedFileName(src) + ".wdf")
	result = LoadTexture(out,flag)
	If result=0
		result = LoadTexture(src,flag)
	EndIf
	If result=0
		If src$="load.jpg"
			Print ""
			Print "Please ensure that you install any updates INTO"
			Print "your existing Wonderland Adventures directory"
			Print "(i.e. overwriting some of the existing files)."
			Print ""
			Print "Do not simply run the .exe file from within the"
			Print "update files, or you will receive this error."
			Print "Unzip the files into your Wonderland directory first."					
			Delay 1000
			Print ""
			Print "Exiting... Press Any Key."
			WaitKey()					
			End
		Else
			FileNotFoundError("Couldn't load Texture: " + src,src,out)
		EndIf
	EndIf
	Return result
End Function

Function MyLoadMesh(src$,parent=0)
	out$ = EncryptedFileName(src)
	If Lower$(Right$(src$,3))="3ds"
		out = out + ".wd3"
	Else
		out = out + ".wd1"
	EndIf
	out = OverideGameData(out)
	dest$ = GlobalDirName$+"/Temp/"+GetFileNameFromPath(src)
	CopyFile out,dest
	If parent>0
		result = LoadMesh(dest,parent)
	Else
		result = LoadMesh(dest)
	EndIf
	DeleteFile dest
	If result=0
		FileNotFoundError("Couldn't load Mesh: " + src, src, out)
	EndIf
	Return result
End Function

Function MyLoadAnimMesh(src$,parent=0)
	out$ = EncryptedFileName(src)
	If Lower$(Right$(src$,3))="3ds"
		out = out + ".wd3"
	Else
		out = out + ".wd1"
	EndIf
	;out = OverideGameData(out)
	dest$ = GlobalDirName$+"/Temp/"+GetFileNameFromPath(src)
	CopyFile out,dest
	If parent>0
		result = LoadAnimMesh(dest,parent)
	Else
		result = LoadAnimMesh(dest)
	EndIf
	DeleteFile dest
	If result=0
		FileNotFoundError("Couldn't load AnimMesh: " + src, src, out)
	EndIf
	Return result
End Function

Function MyLoadMD2(src$)
	out$ = EncryptedFileName(src) + ".wd2"
	result = LoadMD2(out)
	If result=0
		result=LoadMD2(src)
		If result=0
			FileNotFoundError("Couldn't load MD2: " + src, src, out)
		EndIf
	EndIf
	Return result
End Function

; FILE SYSTEM
; ============
Function MyFileType(src$)
	Return FileType(EncryptedFileName(src) + ".wdf")
End Function

Function OverideGameData$(ex$)
	If CompleteCustomHub
		For i=0 To CompleteCustomHubOverideDataCount-1
			;DebugLog CompleteCustomHubOverideData$(i)+"="+ex$
			If Lower(CompleteCustomHubOverideData$(i))=Lower(ex$)
				;DebugLog CompleteCustomHubsDirectory+"/"+CompleteCustomHubName+"/"+ex$
				Return CompleteCustomHubsDirectory+"/"+CompleteCustomHubName+"/"+ex$
			EndIf
		Next
	EndIf
	Return ex$
End Function

Function EncryptedFileName$(src$)
	dir$ = GetDirNameFromPath(src)
	inFile$ = Lower(GetFileNameFromPath(src, False))
	outFile$ = ""
	leng = Len(inFile$)
	For i=1 To leng
		ch = Asc(Mid(inFile,i,1))
		If ch>=97 And ch<=122
			ch = ch+1
			If ch=123 Then ch=97
		EndIf
		outFile = outFile+Chr(ch)
	Next
	Return dir+outFile
End Function

Function FileNotFoundError(message$, file$, file_enc$)
	jj=0
	Repeat
		jj=jj+1
	Until FileType("debug."+Str$(jj))=0		
	debugfile=WriteFile ("debug."+Str$(jj))
	Print message$
	Delay 5000
	WriteString debugfile,file$
	WriteString debugfile,file_enc$
	If FileType(file_enc$)=1
		WriteString debugfile,"File Exists!"
	Else
		WriteString debugfile,"File Doesn't Exist!"
	EndIf
	WriteInt debugfile,TotalVidMem()
	WriteInt debugfile,AvailVidMem()
	CloseFile debugfile
	End
End Function