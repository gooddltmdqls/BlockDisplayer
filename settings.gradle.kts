
rootProject.name = "BlockDisplayer"
include("src:main:utils")
findProject(":src:main:utils")?.name = "utils"
