import {HttpErrorResponse} from "@angular/common/http";

export const buildErrorMessage = (error: HttpErrorResponse) => {
  if(error.status === 404) return 'Resource not found'
  return 'Something went wrong. Please try again.'
}
