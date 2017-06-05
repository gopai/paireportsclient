# paireportsclient

The API allows you to filter reports using key-value pairs and also enables you to alter the columns’ visibility.
The format for filtering columns is 
                              F_Column name=Column filter
  where the column name is the key and the column filter is the value.
An example would be a column name of “City” with a value of “Billings,” in this case the F_ would show the filter being performed on the 
column.
The visibility format includes an ED_ clause and has the format 
                       F_Column name=Column filter&ED_Column name=true
The visibility can be set to either true or false which displays or hides the column respectively.
An example URL could be 
                     GetNewUserReport.event?ReportCmd=Filter&F_City=Billings&ED_City=false
