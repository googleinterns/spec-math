import { ComponentFixture } from '@angular/core/testing';
import { By } from '@angular/platform-browser';

export const queryElement = (targetFixture: ComponentFixture<any>, targetClass: string) => {
  return targetFixture.debugElement.query(By.css(targetClass));
};
