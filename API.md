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
 * CSV response format: `ReportCmd=CustomCommand&CustomCmdList=DownloadCSV`
 * XML response format: `RenderType=xml`
 * HTML default configuration

### Filters
The API allows you to filter reports using key-value pairs. The format for filters is
 
`F_[Column name]=[Column filter]`

We can see that there are two attributes for a column in the above line: Column Name and Column Filter. The Column Name, as the name suggests, is the name of the column and is the key portion of the filter statement. The Column Filter will be applied as a filter to that column and is the value of the key.

The F_ indicates that this is a filter for a column. An example would be a Column Name of “City” with a Column Filter of “Billings” (e.g. `F_City=Billings`). In this case, the F_ would indicate that in the “City” column, we want results that contain the text "Billings."

### Visibility
We can take this a step further by adding visibility to columns. Visibility simply lets us show, or hide, a given column and can be set to either true or false. Setting the visibility to true shows the column and populates the filter if applicable. Assigning it a value of false removes the column from the report. The format for visibility is

`E_[Column Name]=[true|false]`

`E_` indicates to the Report API that the key indicates which column will have its visibility adjusted. The visibility will be adjusted by the value. The value can either be `true` or `false`. False indicates that the column should not be included and true indicates that the column should be shown. Filters will still apply even if the column is not shown.

The following example will filter the report by narrowing to only usernames that contain the text 'bill' and will not include the column Username in the final result.

`F_Username=bill&E_Username=false`

### Putting it all together
A full request to the Report API will look like:

```
POST /myreports/GetNewUserReport.event HTTP/1.1
Host: paireports.com
Content-Type: application/x-www-form-urlencoded
Cookie: JSESSIONID=94F35B89002804BE569C3D1A2A12BC95.IoI;
User-Agent :Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.89 Safari/537.36
Content-Length: 89

ReportCmd=Filter&CustomCommand&CustomCmdList=DownloadCSV&F_Username=bill&E_Username=false
```

This will issue a request to the Report API to retrieve the Users Report filtering by usernames that are like bill and disabling the username column. It will use the default report configuration as no ReportGUID was selected and it will return the content in CSV format.

## Login Process
The login process requires an HTTP POST request. Several headers are required including `User-Agent`, `Content-Type`, and a `Method` of `POST`.
```
URL url = new URL(BASE_URL + "Login.event");
HttpURLConnection con = (HttpURLConnection) url.openConnection();
con.setRequestProperty("User-Agent", "Mozilla/5.0");
con.setDoOutput(true);
con.setInstanceFollowRedirects(false);
con.setRequestProperty("Accept", "application/json");
con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
con.setRequestMethod("POST");
```
What is required for further requests is a `JSESSIONID`. This token represents your session with PAI Reports and is required for all communications. This parameter is communicated to the server using the Cookie header. The JSESSIONID can be obtained through Cookie management and will also be returned by the Login event on successful login.
```
con.setRequestProperty("Cookie", "JSESSIONID=94F35B89002804BE569C3D1A2A12BC95.IoI;");
```
The login  event ```Login.event``` requires `Username` and `Password`. These two fields must be transmitted in `application/x-www-form-urlencoded` format.

<p>Since we use cookies, once a login is performed and authenticated, a session token is provided back to the client from the server. Our API stores this session token in a variable and will be used to identify a specific login occurrence. A representation of this client-server communication is below.</p> 

![alt text](https://github.com/gopai/paireportsclient/blob/master/client-server%20diagram.png)
