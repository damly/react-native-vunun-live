'use strict';

import React, {Component, PropTypes} from 'react';
import {
    NativeModules,
    requireNativeComponent,
    View,
    Platform
} from 'react-native';

var NativeLiveViewManager;
var NativeLiveView;

if (Platform.OS === 'android') {
    NativeLiveViewManager = NativeModules.RNLibReStreamingModule;
}
else {
    NativeLiveViewManager = NativeModules.RNVideoCoreViewManager;
}

class LiveView extends Component {

    constructor(props) {
        super(props);
    }

    componentDidMount() {
        NativeLiveViewManager.startPublish();
    }

    componentWillUnmount() {
        NativeLiveViewManager.stopPublish();
    }

    render() {
        return <NativeLiveView {...this.props}/>;
    }
}

LiveView.propTypes = {
    ...View.propTypes,
    streamUrl:PropTypes.string,
    streamKey:PropTypes.string,
    orientation:PropTypes.string,
    quality:PropTypes.string,
    camera:PropTypes.string
};

if (Platform.OS === 'android') {
    NativeLiveView = requireNativeComponent('RNLibReStreamingView', LiveView);
}
else {
    NativeLiveView = requireNativeComponent('RNVideoCoreView', LiveView);
}

export default LiveView;