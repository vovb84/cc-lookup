# cc-lookup-client

###### Description: Java client to query provided http server to get country names for provided codes. 
###### Language used: Java

## Required files
### target/cc-lookup-client-<current-version>.jar
Actual jar binary to do the job. Usage:  
```
  java -jar target/cc-lookup-client-<current-version>.jar <options>
  java -jar target/cc-lookup-client-0.1-SNAPSHOT.jar <options>
```
  Example:  
   1. get country names for provided csv country codes with 10 parallel queries:  
    ```
      java -jar target/cc-lookup-client-0.1-SNAPSHOT.jar -c " AU  , 23 ,   NL"
    ```  
    Outcome:  
    23 -> Unknown Country Code  
    AU -> Australia  
    NL -> Netherlands  

   2. collect page and save it to the file only:  
    ```
      java -jar target/cc-lookup-client-0.1-SNAPSHOT.jar -g
    ```
   Outcome:  
    File /tmp/sony/data.jsonis written successfully.  
   
  3.  get all this info from already collected file:  
    ```
      java -jar target/cc-lookup-client-0.1-SNAPSHOT.jar -c " AU  , 23 ,   NL" -p
    ```

  Options:
  
    -n <number-of-concurrent-requests> - number of concurrent requests.
        Limited to 100. Optional. [default]: 10.

    -q <http-addr> - server http address + page.
        Optional. [default]: status.

    -d <local-dir> - Local dir to save page.
        Optional. [default]: /tmp/sony
        
    -c - Country codes separated by comma.
        Ex: AU
            "AU, AX"
            "  AU ,   AX   "
            etc.
    
    -p - Parse already collected file only.
        Optional. [default]: false.
        
    -g - Collect page to the file only.
        Optional. [default]: false.
    
    -h show help.

    Notes;
      1. -c argument is required in all situations except -g.
      2. If -n is > then 100 it automatically got limited to 100.
      3. Log file is located in /usr/local/var/log. To change the location and log
         level - edit src/main/resources/log4j.properties file and recompile app.



## First time desktop/laptop config
To use client itself the following config should be done:
### MAC OS or Linux:
- Verify you have Java SE 8 (build 1.8.X) installed and active on your laptop:
```
   java -version
```
  You should have this kind of reply:  
  ```
     java version "1.8.0_181"
     Java(TM) SE Runtime Environment (build 1.8.0_181-b13)
     Java HotSpot(TM) 64-Bit Server VM (build 25.181-b13, mixed mode)
  ```
  If you don't have it installed - you can download the latest version:
  ```
     https://www.oracle.com/technetwork/java/javase/downloads/index.html
  ```
  Note: Linux standard maven installation (yum/apt-get) will do Java SE 8 installation as well.  

- Install Maven. Instructions: https://maven.apache.org/install.html
- clone the package to your laptop/desktop
- run 
```
   mvn clean
   mvn install
```

## If you already have it pulled previously
### MAC OS or Linux:
- pul latest changes from 'master' branch:
```
   git pull origin master
```
- clean and recompile jar files;
```
   mvn clean
   mvn install
```
Note:
- You might see some WARNINGs during compilation:
```
   [WARNING] Discovered module-info.class. Shading will break its strong encapsulation.
   ...
```
   Just ignore them...

## Contributing
1. Fork the repository on Github
2. Create a named feature branch (like `add_component_x`)
3. Write your change
4. Write tests for your change (if applicable)
5. Run the tests, ensuring they all pass
6. Add section to this README.md file
7. Submit a Pull Request using Github

## Authors
Authors: 
Vlad Borissov


