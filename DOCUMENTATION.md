
# paireportsclient

## API Documentation
<p>•	The protocol being used is HTTP and requests are handled with the POST method instead of GET. This is because POST provides the payload from the client to the server.</p>
<p>•	The URL is encoded using UTF-8 since we may get a string with special characters or spaces. When that happens, the string will be converted to the format</p>  
                                     <p>application/x-www-form-urlencoded</p> 
<p>•	The user agent is also specified to be Mozilla/5.0. Failing to do this properly results in the API acting as if you are on mobile.</p> 
<p>•	The Accept header shows that the response being expected is JSON and the Content-Type header indicates that the data being sent in the request is also JSON since both headers are set to</p>  
                                                <p>application/json</p> 
<p>•	After all of this is accomplished, an OK should be the response.</p> 
<p>•	Next, after the username and password have been sent to the server and verified as correct, a session ID is received from the server and then stored by the client.</p> 
<p>•	The API allows you to filter reports using key-value pairs and also enables you to alter the columns’ visibility.</p> 
<p>•	The format for filtering columns is</p>  
                                                             <p>F_Column name=Column filter</p> 
            <p>where the column name is the key and the column filter is the value.</p> 
<p>•	An example would be a column name of “City” with a value of “Billings,” in this case the F_ would show the filter being performed on the column.</p> 
<p>•	The visibility format includes an ED_ clause and has the format</p>  
                                            <p>F_Column name=Column filter&ED_Column name=true</p> 
<p>•	The visibility can be set to either true or false which displays or hides the column respectively.</p> 
<p>•	An example URL could be</p>  
                     <p>GetNewUserReport.event?ReportCmd=Filter&F_City=Billings&ED_City=false</p>
                     
<p>•  ReportCmd=Filter indicates that the command to be performed on the report is a filter.</p>

![alt text](https://github.com/gopai/paireportsclient/blob/master/client-server%20diagram.png)
