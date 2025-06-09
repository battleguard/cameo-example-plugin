# How to run

1. create a `gradle.properties` in this directory with the following:

> WARNING: only works with Cameo 2022. You will have to update the plugin.xml and verify all imports are the same to use other versions.

```txt
cameoInstallDir=<path-to-cameo-install-directory>
```
Example Path: `cameoInstallDir=C:\\portable_programs\\cameo-installs\\Cameo-Systems-Modeler-2022x-Enterprise`

2. Open the build.gradle in intellij and click Load Gradle Project

3. Open the `build.gradle` in intellij and click the green arrow next to the `debugJar` task to run or debug. 