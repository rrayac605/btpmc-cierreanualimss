#Introducción
Batch encargado de procesar los datos de las casuísticas provenientes del RFC IMSS IMS421231I45, alojadas en mongodb y enviarlas en formato de archivo a dos sftp.

#Variables de entorno

============== AMBIENTE DE QA ==============

Para que este batch pueda ser ejecutado deberán de existir las siguietnes variables de entorno en el SO annfitrión donde se ejecute el jar

- portCierreAnualImssBatch: 9021
- authenticationdatabaseMongo: PMCQA01
- usrMongoCifras: pmcmodifica
- pwMongoCifras: pmcmodifica0
- databaseMongo: PMCQA01
- portMongo: 27017
- hostMongo: 10.100.8.78
- fileLogBatchCierreAnualImss: /weblogic/pmc/logs/btpmc-cierreanualimss.log
- cierreAnualImssBatchJunio: 0 0 23 30 JUN ?
- cierreAnualImssBatchEnero: 0 0 23 14 JAN ?
- cierreAnualImssBatchFebrero: 0 0 23 19 FEB ?
- sftpPath: /pmc_usr/archivoscierreanualRFCIMSS/{year}/{month}/ 
- sftpHost: 10.100.6.76
- sftpPort: 22
- sftpUser: root
- sftpPass: Passw0rd

============== AMBIENTE DE UAT ==============

Para que este batch pueda ser ejecutado deberán de existir las siguietnes variables de entorno en el SO annfitrión donde se ejecute el jar

- portCierreAnualImssBatch: 9021
- authenticationdatabaseMongo: PMCUAT01
- usrMongoCifras: pmcmodifica
- pwMongoCifras: pmcmodifica0
- databaseMongo: PMCUAT01
- portMongo: 27017
- hostMongo: 10.100.8.80
- fileLogBatchCierreAnualImss: /weblogic/pmc/logs/btpmc-cierreanualimss.log
- cierreAnualImssBatchJunio: 0 0 23 30 JUN ?
- cierreAnualImssBatchEnero: 0 0 23 14 JAN ?
- cierreAnualImssBatchFebrero: 0 0 23 19 FEB ?
- sftpPath: /home/usr_pmc/archivoscierreanualRFCIMSS/{year}/{month}/
- sftpHost: 10.100.6.98
- sftpPort: 22
- sftpUser: usr_pmc
- sftpPass: Pmc.Usr.&


============== AMBIENTE DE PROD ==============

Para que este microservicio pueda ser ejecutado deberán de existir las siguietnes variables de entorno en el SO annfitrión donde se ejecute el jar

- portCierreAnualImssBatch: Pendiente
- authenticationdatabaseMongo: Pendiente
- usrMongoCifras: Pendiente
- pwMongoCifras: Pendiente
- databaseMongo: Pendiente
- portMongo: Pendiente
- hostMongo: Pendiente
- fileLogBatchCierreAnualImss: Pendiente
- cierreAnualImssBatchJunio: 0 0 23 30 JUN ?
- cierreAnualImssBatchEnero: 0 0 23 14 JAN ?
- cierreAnualImssBatchFebrero: 0 0 23 19 FEB ?
- sftpPath: /home/usr_pmc/archivoscierreanualRFCIMSS/{year}/{month}/
- sftpHost: Pendiente
- sftpPort: Pendiente
- sftpUser: Pendiente
- sftpPass: Pendiente



