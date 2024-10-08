# Btyhub - Lib common
![GitHub Release](https://img.shields.io/github/v/release/Magiavventure/lib-common)
[![codecov](https://codecov.io/gh/Magiavventure/lib-common/graph/badge.svg?token=S049F9NFTF)](https://codecov.io/gh/Magiavventure/lib-common)
![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/Magiavventure/lib-common/build.yml)

This common library handles errors with a general handler and request and response logs from all services.
It also inserts the transactionId header to aid log searching.

## Configuration

The properties exposed to configure this project are:

```properties
logging.level.com.btyhub="string"                                                 # Logging level package btyhub
btyhub.lib.common.errors.errors-messages.{error-key}.code="string"                # The exception key error code
btyhub.lib.common.errors.errors-messages.{error-key}.message="string"             # The exception key error message
btyhub.lib.common.errors.errors-messages.{error-key}.description="string"         # The exception key error description
btyhub.lib.common.errors.errors-messages.{error-key}.status=integer               # The exception key error status
```


## Error message map
The error message map is a basic system for return the specific message in the error response,
the configuration path at the moment is only for one branch **errors-messages**.
This branch setting a specific error message to **com.btyhub.lib.common.error.BtyhubException**