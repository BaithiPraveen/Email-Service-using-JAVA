<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Email Template</title>
    <style>
        body {
            font-family: Arial, sans-serif;
        }
        .container {
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        h1 {
            color: #333;
        }
        p {
            color: #666;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Hello ${recipientName},</h1>
         <#if description??>
            <p>${description}</p>
        <#else>
            <p>This is a sample email template generated using FreeMarker.</p>
            <p>We hope you find this example helpful for creating your own email templates.</p>
        </#if>
        <p>This is a sample email template generated using FreeMarker.</p>
        <p>Best regards,</p>
        <p>${senderName}</p>
        <p>${location}</p>
    </div>
</body>
</html>
