hadoop fs -rm -r /user/s000650a/search_org_profile/output
hadoop fs -mkdir /user/s000650a/search_org_profile/output
hadoop fs -ls /user/s000650a/search_org_profile/output
#hadoop fs -setfacl -R 
hadoop fs -setfacl -R -m group:solr:rwx,mask::rwx /user/s000650a/search_org_profile
hadoop fs -setfacl -R -m default:group:solr:rwx,default:mask::rwx /user/s000650a/search_org_profile/
hadoop fs -getfacl /user/s000650a/search_org_profile/
hadoop fs -getfacl /user/s000650a/search_org_profile/output

