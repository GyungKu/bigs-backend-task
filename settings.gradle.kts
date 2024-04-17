rootProject.name = "bigs-backend-task"

include("modules")
include("modules:application", "modules:domain", "modules:internal")
include("modules:application:sync-application", "modules:application:inquire-application")