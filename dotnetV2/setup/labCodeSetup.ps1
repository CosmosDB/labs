param($codePath = "$Home\Documents")

$targetPath = "$codePath\CosmosLabs"

try{
    New-Item -Name CosmosLabs -Path $codePath -ItemType Directory -ErrorAction Stop
}
catch{
    $directoryInfo = Get-ChildItem $targetPath | Measure-Object

    if ($directoryInfo.count -eq 0){
        Write-Output "Found existing folder '$targetPath'"
    }
    else{
        Write-Output "Folder '$targetPath' has existing files. Please delete or move files from the folder before starting new lab code setup to avoid conflicts."
        exit
    }
}

Copy-Item -Path .\templates\* -Filter "*.*" -Recurse -Destination $targetPath -Container -Force

Write-Output "" "Copied all lab code files to '$targetPath'"

