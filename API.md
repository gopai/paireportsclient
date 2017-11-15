# Example Client for PAI Reports Report API

<p>
This is a sample application that demonstrates interfacing with paireports.com to receive data. Using the Report API provided by PAI you can build processes that will extract data based on the criteria that you desire. 
</p>
<p>
The API is based on standard HTTP practices and primarily uses key-value pairs for passing filters, sort orders, and report selection. The documentation below describes the available commands.
</p>

## API Documentation
The example application has a demonstration of how to login and establish a session with PAI Reports. This documenation will not go into those details.

After a session has established retrieving data requires four peices of data. 
 * Report Path (ex. `/GetNewUserReport.event`)
  * Operation
 * predefined configuration identifier, if desired (ex. `ReportGUID=14E0CB59-CE95-E411-8519-D4AE52896C05`)
 * data format of the result (ex. `CustomCommand&CustomCmdList=DownloadCSV` to download as CSV format)
 * filters and sort orders that you desire for this run. (`F_Username=Bill`)


### Configuration
Configurations can be transmitted either by URL or in the data segment of an HTTP POST request. Requests must be formatted in key-value pairs using `x-www-form-urlencoded` encoding. URL Encoding must be used with configurations passed both the URL and the data segment. Reports will answer to both GET or POST requests, but POST requests are recommended to prevent caching.  

### Operation
The configuration `ReportCmd=Filter` should always be included. This instructs the report to gather data and retrieve it. If this configuration is not added the report will either return the same result as it did in the previous run or will return no results.

### Report Path
Report within PAI Reports can be accessed directly by URL. For example, if you access `https://www.paireports.com/myreports/GetNewUserReport.event` you be be presented with the users report if you have a valid session. PAI Reports requires the use of a secure connection. Make sure that you don't forget to establish your connection using HTTPS. 

### Predefined Configuration
PAI Reports allows the report to be configurated in many ways including the order of the column, predefined filters, and sort orders. Creating these configurations can be done through the standard user interface within PAI Reports. Programmatically creating these configurations is not discussed in this document. The data can be retrieved using the predefined configuration by passing the configuration like so `ReportGUID=14E0CB59-CE95-E411-8519-D4AE52896C05`. The key `ReportGUID` instructs the Report API to select the configuration defined by the GUID value. In the example above the report GUID is `14E0CB59-CE95-E411-8519-D4AE52896C05`. Currently, the only way to find these configuration ids is to use an inspection tool within a web browser and looking at the source of the dropdown on the report screen. This may change in the future with an inclusion of the GUID within the configuration screen.

### Data Format
PAI Reports currently only supports CSV, XML, and HTML response formats.
 * CSV response format: `CustomCommand&CustomCmdList=DownloadCSV`
 * XML response format: `RenderType=xml`
 * HTML default configuration

### Filters
<p>The API allows you to filter reports using key-value pairs. The format for filters is</p>
`F_Column name=Column filter`
<p>We can see that there are two attributes of the column in the above line, column name and column filter. The column name, as the name suggests, is the name of the column and is the key portion of the filter statement. The column filter identifies a particular aspect of the key that we would like to focus on, and by this association, the column filter is the value of the key.</p>

<p>The F_ shows the filter being performed on the column. An example would be a column name of “City” with a value of “Billings,” in this case the F_ would indicate that in the “City” column, we want the ones called "Billings."</p>

### Visibility
<p> We can take this a step further by adding visibility to columns. Visibility simply lets us show or hide a given column and can be set to either true or false. Setting the visibility to true populates the filter and assigning it a value of false removes the column from the report. The format for visibility is</p>
`E_Column name=true|false`
<p>`E_` indicates to the Report API that the rest of the key indicates the column and that visibility will be adjusted based on the value. The value can either be `true` or `false`. False indicates that the column should not be included and true indicates the column should be shown. Filters will still apply even if the column is not shown.</p> 
`F_Username=bill&E_Username=false`
<p>This example will filter the report by narrowing to only usernames that loosely match the name bill and will not include the column in the final result.</p>


<p>•	A more concrete URL example of both filter and visibility is as follows</p>
                 <p>GetNewUserReport.event?ReportCmd=Filter&F_City=Billings&ED_City=false</p> 
<p>•	More characters are again included in the line. The “&” before the filter clause serves the same purpose it did between the filter and visibility parts.</p>
<p>•	ReportCmd=Filter says that the action being performed on the report is a filter and the rest of the statement states that we are getting a user report. The .event is a suffix used be PAI.</p>
<p>•	We are now done looking at filters and visibility and we’ll procede to look at other properties of the API.</p>
<p>•	The client communicates with the server via the Hypertext Transfer Protocol (HTTP) and various headers.</p>
<p>•	The first header involves handling requests. Requests are handled with the POST method instead of the GET method. This is because POST provides the payload from the client to the server while GET cannot.</p>
<p>•	Another point to mention is that our URL is URLencoded using UTF-8. This is accomplished by the Content-Type header. URL encoding is vital since we may get a URL string with special characters or spaces. When that happens, the string will be converted to the format</p>
                                                 <p>application/x-www-form-urlencoded</p>
<p>•	There is a second Content-Type header which indicates that the data being sent in the request is json and that involves giving the header the string</p>
                                                    <p>application/json</p>
<p>•	The fourth header is the Accept, which shows which response is expected. In our API the response is json because the Accept header is assigned the same string value as the Content-Type responsible for request.</p>
<p>•	Next we take a look at the User-Agent header. This allows us to set our browser preference. Our API specifies Mozilla/5.0. Failing to do this properly results in the API acting incorrectly such as thinking that we are on mobile.</p>
<p>•	Once all of the headers have been set, we get an OK response.</p>
<p>•	The API requires login tokens which are a username and a password.</p>
<p>•	Since we use cookies, once a login is performed and authenticated a session token is provided back to the client from the server. Our API stores this session token in a variable and will be used to identify a specific login occurrence. A representation of this client-server communication is below.</p> 

![alt text](https://github.com/gopai/paireportsclient/blob/master/client-server%20diagram.png)
