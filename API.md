# Example Client for PAI Reports Report API

<p>
This is a sample application that demonstrates interfacing with paireports.com to receive data. Using the Report API provided by PAI, you can build processes that will extract data based on the criteria that you provide. 
</p>
<p>
This API is based on standard HTTP practices and primarily uses key-value pairs for passing filters, sort orders, and report selections. The documentation below describes the available commands.
</p>

## API Documentation
The example application is a demonstration of how to login and establish a session with PAI Reports. This documenation will not go into those details.

After a session has been established, retrieving data requires four pieces of data. 
 * Report Path (ex. `/GetNewUserReport.event`)
  * Operation
 * predefined configuration identifier, if desired (ex. `ReportGUID=14E0CB59-CE95-E411-8519-D4AE52896C05`)
 * data format of the result (ex. `CustomCommand&CustomCmdList=DownloadCSV` to download as CSV format)
 * filters and sort orders that you desire for this run. (`F_Username=Bill`)


### Configuration
Configurations can be transmitted either by URL or in the data segment of an HTTP POST request. Requests must be formatted in key-value pairs using `x-www-form-urlencoded` encoding. URL Encoding must be used with configurations passed through both the URL and the data segment. Reports will answer to both GET or POST requests, but POST requests are recommended to prevent caching.  

### Operation
The configuration `ReportCmd=Filter` should always be included. This instructs the report to gather data and return it. If this configuration is not added, the report will either return the same result as it did in the previous run or will return no results.

### Report Path
Reports within PAI Reports can be accessed directly by URL. For example, if you access `https://www.paireports.com/myreports/GetNewUserReport.event` you will be presented with the Users Report.  PAI Reports requires the use of a secure connection so you will need a valid session to access a report. Make sure that you establish your connection using HTTPS. 

### Predefined Configuration
PAI Reports allows reports to be configured in many ways. This includes the column order, predefined filters, and sort orders. Creating these configurations can be done through the standard user interface within PAI Reports. Programmatically creating these configurations is not discussed in this document. The data can be retrieved using the predefined configuration by passing the configuration GUID (ex. `ReportGUID=14E0CB59-CE95-E411-8519-D4AE52896C05`). The key `ReportGUID` instructs the Report API to select the configuration defined by the GUID value. In the example above, the report GUID is `14E0CB59-CE95-E411-8519-D4AE52896C05`. Currently, the only way to find these configuration IDs is to use an inspection tool within a web browser and looking at the source of the dropdown on the report screen. This may change in the future with an inclusion of the GUID within the configuration interface.

### Data Format
PAI Reports currently only supports CSV, XML, and HTML response formats.
 * CSV response format: `CustomCommand&CustomCmdList=DownloadCSV`
 * XML response format: `RenderType=xml`
 * HTML default configuration

### Filters
<p>The API allows you to filter reports using key-value pairs. The format for filters is</p>
`F_[Column name]=[Column filter]`
<p>We can see that there are two attributes for a column in the above line: Column Name and Column Filter. The Column Name, as the name suggests, is the name of the column and is the key portion of the filter statement. The Column Filter will be applied as a filter to that column and is the value of the key.</p>

<p>The F_ indicates that this is a filter for a column. An example would be a Column Name of “City” with a Column Filter of “Billings” (e.g. F_City=Billing). In this case, the F_ would indicate that in the “City” column, we want results that contain the text "Billings."</p>

### Visibility
<p> We can take this a step further by adding visibility to columns. Visibility simply lets us show, or hide, a given column and can be set to either true or false. Setting the visibility to true shows the column and populates the filter if applicable. Assigning it a value of false removes the column from the report. The format for visibility is</p>
`E_[Column Name]=[true|false]`
<p>`E_` indicates to the Report API that the key indicates which column will have its visibility adjusted. The visibility will be adjusted by the value. The value can either be `true` or `false`. False indicates that the column should not be included and true indicates that the column should be shown. Filters will still apply even if the column is not shown.</p> 
<p>The following example will filter the report by narrowing to only usernames that contain the text 'bill' and will not include the column Username in the final result.</p>
`F_Username=bill&E_Username=false`


<p>•	A more concrete URL example of both filter and visibility is as follows</p>
                 <p>GetNewUserReport.event?ReportCmd=Filter&F_City=Billings&E_City=false</p> 
<p>•	Additional characters were included in the example. The “&” after the filter clause serves the same purpose as it did between the filter and visibility clauses.</p>
<p>•	ReportCmd=Filter says that the action being performed on the report is filter while the rest of the statement states that we are getting a user report. The .event is a suffix used be PAI.</p>
<p>We are now done looking at filters and visibility and we will now procede to look at other properties of the API.</p>
<p>•	The client communicates with the server via the Hypertext Transfer Protocol (HTTP) using various headers.</p>
<p>•	The first header involves handling requests. Requests are handled with the POST method instead of the GET method. The reason is that POST provides the payload from the client to the server while GET does not.</p>
<p>•	Another point to mention is that our URL is encoded using UTF-8. This is accomplished by using the Content-Type header. URL encoding is vital since we may get a URL string with special characters or spaces. When that happens, the string will be converted to the format</p>
                                                 <p>application/x-www-form-urlencoded</p>
<p>•	There is a second Content-Type header which indicates that the data being sent in the request is json. This involves giving the header the string</p>
                                                    <p>application/json</p>
<p>•	The fourth header is Accept. This shows which response is to be expected. In our API, the response is json because the Accept header is assigned the same string value as the Content-Type responsible for the request.</p>
<p>Next we will take a look at the User-Agent header. This allows us to set our browser preference. PAI Reports' API specifies Mozilla/5.0. Failing to do this properly will result in the API behaving incorrectly, such as thinking that we are on a mobile device.</p>
<p>•	Once all of the headers have been set, an OK response will be sent.</p>
<p>•	The API requires a login token, which is a username and a password.</p>
<p>•	Since we use cookies, once a login is performed and authenticated, a session token is provided back to the client from the server. Our API stores this session token in a variable and will be used to identify a specific login occurrence. A representation of this client-server communication is below.</p> 

![alt text](https://github.com/gopai/paireportsclient/blob/master/client-server%20diagram.png)
