Function Clamp#(x#, lower_limit#, upper_limit#)
	If x < lower_limit Then x = lower_limit
	If x > upper_limit Then x = upper_limit
	Return x
End Function

Function SmoothStep#(edge_0#, edge_1#, x#)
	x = Clamp((x - edge_0) / (edge_1 - edge_0), 0.0, 1.0)
	Return x * x * (3 - 2 * x)
End Function

Function CleanDir(ex$)
	; completely cleans the specified directory and also deletes it
	dir=ReadDir(ex$)
	Repeat
		ex2$=NextFile$(dir)
		If ex2$<>"" And ex2$<>"." And ex2$<>".."
			filename$=ex$+Chr$(47)+ex2$
			If FileType(filename$)=1
				DeleteFile filename$
			Else If FileType(filename$)=2
				CleanDir(filename$)
			EndIf
		EndIf
	Until ex2$=""
	If ex$<>GlobalDirName$+"/Player Profiles/"+PlayerName$+"/Current/Adventure" And ex$<>GlobalDirName$+"/Player Profiles/"+PlayerName$+"/Current/Hub"
		DeleteDir ex$
	EndIf
End Function

Function MyCreateDir(dirpath$)
	; creates all the sub directories along with their parent directories
	For i=1 To Len(dirpath$)
		If Mid$(dirpath$,i,1)=Chr$(47) Then
			folder$=Left$(dirpath$,i-1)
			d=ReadDir(folder$)
			If d Then 
				CloseDir(d)
			Else
			    CreateDir(folder$)
				If FileType(folder$)=0 Then RuntimeError("Couldn't create directory: "+folder$)
			EndIf
		EndIf
	Next
End Function

Function GetFileNameFromPath$(path$, ext = True)
	; fetches the exact file name from path and removes the rest of the path
	For i=Len(path$) To 1 Step -1
		If Mid$(path$,i,1)=Chr$(47) Or Mid$(path$,i,1)=Chr$(92)
			file$ = Right$(path,Len(path$)-i)
			If (ext = True)
				Return file$
			Else
				For i=Len(file$) To 1 Step -1
					If Mid$(file$,i,1) = "."
						Return Left(file$,i-1)
					EndIf
				Next
			EndIf
		EndIf
	Next
	If (ext = True)
		Return path$
	Else
		For i=Len(path$) To 1 Step -1
			If Mid$(path$,i,1) = "."
				Return Left(path$,i-1)
			EndIf
		Next
	EndIf
End Function

Function GetDirNameFromPath$(path$)
	; fetches the exact directory name
	For i=Len(path$) To 1 Step -1
		If Mid$(path$,i,1)=Chr$(47) Or Mid$(path$,i,1)=Chr$(92)
			Return Left$(path,i)
		EndIf
	Next
	Return ""
End Function

Function CopyDirFiles(src$,dest$)
	dir=ReadDir(src$)
	Repeat
		ex$=NextFile(dir)
		If ex$<>"." And ex$<>".." And ex$<>""
			filepath$=src$+"/"+ex$
			If FileType(filepath$)=1
				CopyFile filepath$,dest$+"/"+ex$
			ElseIf FileType(filepath$)=2
				CreateDir dest$+"/"+ex$
				CopyDirFiles(filepath$,dest$+"/"+ex$)
			EndIf
		EndIf
	Until ex$=""
End Function

Function CreateFileFromBytes(name$,src,size)
	destFile=WriteFile(name$)
	For i=0 To size-1
		WriteByte destFile,ReadByte(src)
	Next
	CloseFile destFile
End Function

Function SkipBytes(src,size)
	For i=0 To size-1
		ReadByte(src)
	Next
End Function

; Q - NPC player functionality
Function IsModelNPC(mdlname$)
	if mdlname$="!NPC" Or mdlname$="!PlayerNPC" Return True
	Return False
End Function